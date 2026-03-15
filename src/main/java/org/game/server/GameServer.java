package org.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    int port = 55555;
    List<ClientHandler> clients = new ArrayList<>();

    public void start() {
        try {
            System.out.println("Server is starting...");


            // TODO: connect with the lookup server and fetch users/questions data

            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept(); // blocks until someone connects
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                Thread t = new Thread(clientHandler);
                t.start();
            }

        } catch (Exception e) {
            System.out.println("Error starting server");
            System.out.println(e.printStackTrace(););
        }
    }
}
