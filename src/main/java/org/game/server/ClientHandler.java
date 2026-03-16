package org.game.server;

import org.game.common.models.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User user; // user attributed with current connection/thread

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

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
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
