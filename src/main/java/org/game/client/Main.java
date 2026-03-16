package org.game.client;
public class Main {
    public static void main(String[] args) {
        System.out.println("Client started...");
        GameClient client = new GameClient("localhost", 55555);
        client.start();
    }
}

