package org.game.server;

import java.io.IOException;

public class ClientStateMachine {
    ClientHandler clientHandler;

    public ClientStateMachine(ClientHandler clientHandler)  {
        this.clientHandler = clientHandler;
    }



    public void start() throws IOException {
        String msg;
        while((msg = clientHandler.receiveMsg()) != null) {
            if(msg.trim().equals("-")) {
                clientHandler.emitMsg("Bye");
                break;
            }

            System.out.println("Received from client: " + msg);
        }

    }


    public void handleDisplayHelloState() {

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
