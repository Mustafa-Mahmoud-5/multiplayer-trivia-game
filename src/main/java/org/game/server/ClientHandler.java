package org.game.server;

import org.game.common.errors.CustomException;
import org.game.common.errors.InvalidArgumentException;
import org.game.common.errors.NotFoundException;
import org.game.common.models.User;
import org.game.common.protocol.Message;
import org.game.common.protocol.MessageParser;
import org.game.common.protocol.enums.MessageType;
import org.game.server.models.Team;
import org.game.server.repositories.QuestionRepo;
import org.game.server.repositories.TeamRepo;
import org.game.server.services.AuthService;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    AuthService authService;
    QuestionRepo questionRepo;
    TeamRepo teamRepo;



    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User currentUser; // user attributed with current connection/thread





    public ClientHandler(Socket socket, AuthService authService, QuestionRepo questionRepo, TeamRepo teamRepo) throws IOException {
        this.socket = socket;
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        this.authService = authService;


        this.questionRepo = questionRepo;
        this.teamRepo = teamRepo;

    }

    public void handleException(Exception e) {
        e.printStackTrace();

        String message;
        int code;
        if(e instanceof CustomException) {
            message = ((CustomException) e).getMessage();
            code = ((CustomException) e).getStatus();
        } else {
            code = 500;
            message = "Something Went Wrong, Try Again!";
        }

        String res = MessageParser.generate(MessageType.ERROR.name(),(""+code), message);
        emitMsg(res);

    }

    public User getUser() {
        return currentUser;
    }

    public void emitMsg(String message) {
        out.println(message);
    }

    public String receiveMsg() throws IOException {
        String msg = in.readLine();
        return msg;
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("Socket closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // TODO: Handle Game Control FLow
    @Override
    public void run() {
        try {
            handleAuthenticationState();
            handleDisplayMenuState();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // disconnect
            System.out.println("Client Disconnected...");
            if (currentUser != null) {
                currentUser.setLoggedIn(false);
            }
            closeConnection();
        }
    }




//    Client Finite State Machine

    public void handleAuthenticationState() throws IOException {

        emitMsg("Authenticate. Enter "+ MessageType.REGISTER + " or " + MessageType.LOGIN);

        while(true) {
            try {
                String input = receiveMsg();
                Message msg = MessageParser.parse(input);
                if(msg.getParts().length < 3) {
                    throw new InvalidArgumentException("invalid message format. use command|name|password");
                }

                String name = msg.getParts()[1];
                String pass = msg.getParts()[2];
                MessageType messageType = msg.getMessageType();

                switch (messageType) {
                    case REGISTER -> {
                        User newUser = authService.register(name, pass);
                        this.currentUser = newUser;
                        emitMsg(MessageParser.generate(MessageType.REGISTER_OK.name(), "Registered successfully! Welcome " + name));
                        return;
                    }
                    case LOGIN ->  {
                        authService.login(name, pass);
                        this.currentUser = authService.getUser(name);
                        emitMsg(MessageParser.generate(MessageType.LOGIN_OK.name(), "Login successful! Welcome back " + name));
                        return;
                    }
                    default -> {
                        throw new InvalidArgumentException("Invalid messageType. Try Again");
                    }
                }
            }
            catch (IOException e) {
                throw e;
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void handleDisplayMenuState() throws IOException {

        while(true) {
            try {
                String menuMsg = MessageParser.generate(
                        MessageType.MENU.name(),
                        "1:Single Player",
                        "2:Multiplayer",
                        "3:View Scores",
                        "0:Quit"
                );
                emitMsg(menuMsg);

                String input = receiveMsg();
                if (input == null) throw new IOException("Client disconnected");

                input = input.trim();

                switch (input) {
                    case "1" -> {
                        handleSingleGameState();// after game ends, loop back to menu
                        continue;
                    }
                    case "2" -> {
                        handleMultiplayerMenuState();
                        continue;

                    }
                    case "3" -> {
//                        handleViewScoresState();
                        continue;
                    }
                    case "0" -> {
                        emitMsg(MessageParser.generate(MessageType.BYE.name(), "Goodbye " + currentUser.getUsername() + "!"));
                        return; // exit, thread ends
                    }
                    default -> {
                        throw new InvalidArgumentException("Invalid option. Choose 1, 2, 3, or 0");
                    }
                }
            } catch (IOException e) {
                throw e; // propagate disconnects
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public GameConfig handleGameConfigState() throws IOException {
        String category = null;
        String difficulty = null;
        int questionCount = 0;

        while(category == null) {
            try {
                List<String> categories = questionRepo.getCategories();
                String categoryOptions = String.join("|", categories);
                emitMsg(MessageParser.generate(MessageType.SELECT_CATEGORY.name(), categoryOptions));

                String input = receiveMsg();
                if (input == null) throw new IOException("Client disconnected");

                input = input.trim();

                // Validate category exists (case insensitive)
                String finalInput = input;
                boolean valid = categories.stream().anyMatch(c -> c.equalsIgnoreCase(finalInput));
                if (!valid) {
                    throw new InvalidArgumentException("Invalid category. Choose from: " + categoryOptions);
                }
                category = input;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                handleException(e);
            }
        }

        while(difficulty == null) {
            try {
                List<String> difficulties = questionRepo.getDifficulties();
                String diffOptions = String.join("|", difficulties);
                emitMsg(MessageParser.generate(MessageType.SELECT_DIFFICULTY.name(), diffOptions));

                String input = receiveMsg();
                if (input == null) throw new IOException("Client disconnected");
                input = input.trim();

                String finalInput = input;
                boolean valid = difficulties.stream().anyMatch(d -> d.equalsIgnoreCase(finalInput));
                if (!valid) {
                    throw new InvalidArgumentException("Invalid difficulty. Choose from: " + diffOptions);
                }
                difficulty = input;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                handleException(e);
            }
        }

        while(questionCount <= 0) {
            try {
                emitMsg(MessageParser.generate(MessageType.ENTER_QUESTION_COUNT.name(), "Enter number of questions"));

                String input = receiveMsg();
                if (input == null) throw new IOException("Client disconnected");
                input = input.trim();

                int count = Integer.parseInt(input);
                if (count <= 0) {
                    throw new InvalidArgumentException("Question count must be greater than 0");
                }
                questionCount = count;
            } catch (IOException e) {
                throw e;
            } catch (NumberFormatException e) {
                handleException(new InvalidArgumentException("Please enter a valid number"));
            } catch (Exception e) {
                handleException(e);
            }
        }

        return new GameConfig(category, difficulty, questionCount, null);
    }


    public void handleSingleGameState() throws IOException {
        GameConfig gameConfig = handleGameConfigState();

        emitMsg(MessageParser.generate(MessageType.INFO.name(), "Starting Single Player Game."));
        emitMsg(MessageParser.generate(MessageType.INFO.name(), "Category: " + gameConfig.category + " | Difficulty: " + gameConfig.difficulty + " | Questions: " + gameConfig.questionsCount));

        // TODO: start the game
//        gameService.playSinglePlayer(this, category, difficulty, questionCount);

    }

    public void handleMultiplayerMenuState() throws IOException {

        while(true) {
            try {
                String menuMsg = MessageParser.generate(MessageType.MULTIPLAYER_MENU.name(), "1:Create Team", "2:Join Team", "0:Back");
                emitMsg(menuMsg);

                String input = receiveMsg();


                if (input == null) throw new IOException("Client disconnected");

                input = input.trim();

                switch (input) {
                    case "1" -> {
                        handleCreateTeamState();
                        return;
                    }
                    case "2" -> {
                        handleJoinTeamState();
                        return;
                    }
                    case "0" -> {
                        return; // back to main menu
                    }
                    default -> {
                        throw new InvalidArgumentException("Invalid option. Choose 1, 2, or 0");
                    }
                }
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                handleException(e);
            }
        }
    }



    public void handleCreateTeamState() throws IOException {
        GameConfig gameConfig = handleGameConfigState();
        String opponentName = null;

        while(opponentName == null) {
            try {
                emitMsg(MessageParser.generate(MessageType.ENTER_OPPONENT_TEAM.name(), "Enter opponent team name"));

                String input = receiveMsg();

                if (input == null) throw new IOException("Client disconnected");

                input = input.trim();

                // TOOD: implement team service
//                Team opponent = teamService.getTeam(input);

//                if (opponent == null) {
//                    throw new NotFoundException("Team '" + input + "' not found. It must be created first.");
//                }
//                if (opponent.getName().equals(myTeam.getName())) {
//                    throw new InvalidArgumentException("You cannot play against your own team");
//                }

//                opponentName = input;
//                myTeam.setOpponentTeamName(opponentName);


                // TODO: start game logic make all members in the team wait
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                handleException(e);
            }
        }
    }


    public void handleJoinTeamState() throws IOException {
        Team joinedTeam = null;

        while(joinedTeam == null) {
            try {
                emitMsg(MessageParser.generate(MessageType.ENTER_TEAM_TO_JOIN.name(), "Enter team name to join"));

                String input = receiveMsg();
                if (input == null) throw new IOException("Client disconnected");
                input = input.trim();


                // TODO: handle joining team
//              joinedTeam = teamService.joinTeam(input, this);
                emitMsg(MessageParser.generate(MessageType.TEAM_JOINED.name(), "Joined team '" + joinedTeam.getName()));
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                handleException(e);
            }
        }

        emitMsg(MessageParser.generate(MessageType.WAITING.name(), "Waiting for team leader to start the game..."));

        // TODO: Block until the game is over.
    }



}
