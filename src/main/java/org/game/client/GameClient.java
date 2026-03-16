package org.game.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    String hostname;
    int port;

    public  GameClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start () {
        try {
            Socket socket = new Socket(hostname, port);
            BufferedReader readerFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writerToServer = new PrintWriter(socket.getOutputStream(), true);
            Scanner keyboard = new Scanner(System.in);

            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = readerFromServer.readLine()) != null) {
                        System.out.println(line);
                    }
                    System.exit(0);
                } catch (Exception e) {
                    System.out.println("Reader Thread Error");
                    e.printStackTrace();
                }
            });

            readerThread.start();

            while(keyboard.hasNextLine()) {
                writerToServer.println(keyboard.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
        }

    }
}
