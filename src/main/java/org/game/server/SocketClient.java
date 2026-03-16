package org.game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    int port;
    String hostname;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;


    public SocketClient(String hostname, int port) throws IOException {
        this.port = port;
        this.hostname = hostname;
        this.socket = new Socket(hostname, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

    }

    public void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String sendRequest(String message) throws IOException {
        writer.println(message);
        String res = reader.readLine();
        return res;
    }
}
