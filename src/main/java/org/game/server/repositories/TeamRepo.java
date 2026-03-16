package org.game.server.repositories;

import org.game.common.models.Question;
import org.game.server.models.Team;

import java.util.*;

public class TeamRepo {
    Map<String, Team> teams = new HashMap<>();

    public Collection<Team> getAll() {
        return new ArrayList<>(teams.values());
    }

    public Team getByName(String name) {
        return teams.get(name);
    }

    public Team removeByName(String name) {
        return teams.remove(name);
    }

    public boolean add(Team team) {
        if (team == null || teams.containsKey(team.getName())) {
            return false;
        }
        teams.put(team.getName(), team);
        return true;
    }
}
