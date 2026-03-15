package org.game.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

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
            System.out.println("Client handler closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleMessage(String msg) {
        System.out.println("Handled msg " + msg);
    }

    @Override
    public void run() {
        try {
            String msg;
            while((msg = in.readLine()) != null) {
                if(msg.trim().equals("-")) {
                   System.out.println("Bye");
                    break;
                }

                System.out.println("Received from client: " + msg);
                handleMessage(msg);
            }

        } catch (Exception e) {
            // disconnect
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
