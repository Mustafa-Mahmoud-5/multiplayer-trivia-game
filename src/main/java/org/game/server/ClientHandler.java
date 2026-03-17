package org.game.server;

import org.game.common.models.User;
import org.game.common.protocol.Message;
import org.game.common.protocol.MessageParser;
import org.game.common.protocol.enums.MessageType;
import org.game.server.repositories.QuestionRepo;
import org.game.server.repositories.TeamRepo;
import org.game.server.repositories.UserRepo;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    UserRepo userRepo;
    QuestionRepo questionRepo;
    TeamRepo teamRepo;



    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User user; // user attributed with current connection/thread





    public ClientHandler(Socket socket, UserRepo userRepo, QuestionRepo questionRepo, TeamRepo teamRepo) throws IOException {
        this.socket = socket;
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.teamRepo = teamRepo;

    }


    public User getUser() {
        return user;
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // disconnect
            System.out.println("Client Disconnected...");
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
                    String res = MessageParser.generate(MessageType.ERROR.name(), "missing username or password");
                    emitMsg(res);
                    continue;
                }

                String name = msg.getParts()[1];
                String pass = msg.getParts()[2];
                MessageType messageType = msg.getMessageType();

                switch (messageType) {
                    case REGISTER -> {
                        User user = userRepo.getByUserName(name);
                        if(user != null) {
                            String res = MessageParser.generate(MessageType.ERROR.name(), "Username taken");
                            emitMsg(res);
                            continue;
                        };

                        User newUser = new User(name, pass, "user");
                        newUser.setLoggedIn(true);
                        userRepo.addUser(newUser);
                        return;
                    }
                    case LOGIN ->  {
                        User user = userRepo.getByUserName(name);
                        if(user== null) {
                            String res = MessageParser.generate(MessageType.ERROR.name(), "User not found");
                            emitMsg(res);
                            continue;
                        };

                        if(!user.getPassword().equals(pass)) {
                            String res = MessageParser.generate(MessageType.ERROR.name(), "User not found");
                            emitMsg(res);
                            continue;
                        }

                        user.setLoggedIn(true);
                        String res = MessageParser.generate(MessageType.ERROR.name(), "User not found");
                        emitMsg(res);
                        return;
                    }

                    default -> {
                        emitMsg(MessageType.ERROR + "|" + "Invalid messageType. Try Again");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                emitMsg(MessageType.ERROR + "|" + "catched Invalid messageType. Try Again");
            }
        }
    }

    public void handleLoginState() {

    }

    public void handleRegisterState() {

    }

    public void handleAdminState() {

    }

    public void handleDisplayMenuState() {

    }

    public void handleSingleGameState() {

    }

    public void handleMultiplayerGameState() {

    }

    public void handleGameConfigState() {

    }


    public void handleCreateTeamState() {

    }

    public void handleJoinTeamState() {

    }

}
