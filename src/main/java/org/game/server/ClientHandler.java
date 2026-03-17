package org.game.server;

import org.game.common.errors.CustomException;
import org.game.common.errors.InvalidArgumentException;
import org.game.common.models.User;
import org.game.common.protocol.Message;
import org.game.common.protocol.MessageParser;
import org.game.common.protocol.enums.MessageType;
import org.game.server.repositories.QuestionRepo;
import org.game.server.repositories.TeamRepo;
import org.game.server.repositories.UserRepo;
import org.game.server.services.AuthService;

import java.io.*;
import java.net.Socket;

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
                    throw new InvalidArgumentException("invalid message format. use command|name|password");
                }

                String name = msg.getParts()[1];
                String pass = msg.getParts()[2];
                MessageType messageType = msg.getMessageType();

                switch (messageType) {
                    case REGISTER -> {
                        User newUser = authService.register(name, pass);
                        this.currentUser = newUser;
                        return;
                    }
                    case LOGIN ->  {
                        authService.login(name, pass);
                        return;
                    }
                    default -> {
                        throw new InvalidArgumentException("Invalid messageType. Try Again");
                    }
                }
            } catch (Exception e) {
                handleException(e);
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
