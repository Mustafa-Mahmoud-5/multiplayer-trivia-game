package org.game.server.models;

import org.game.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class Team {
    String name;
    List<ClientHandler> members = new ArrayList<>();
    int score = 0;

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean addMember(ClientHandler clientHandler) {
        boolean isFound = members.stream().anyMatch(client -> client.getUser().getUsername().equals(clientHandler.getUser().getUsername()));
        if(!isFound) {
            members.add(clientHandler);
            return true;
        } else {
            return false;
        }
    }
}
