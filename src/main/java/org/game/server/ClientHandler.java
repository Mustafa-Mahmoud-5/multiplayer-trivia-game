package org.game.server;

import org.game.common.models.User;
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
            ClientStateMachine clientStateMachine = new ClientStateMachine(this);
            clientStateMachine.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // disconnect
            System.out.println("Client Disconnected...");
            closeConnection();
        }
    }
}
