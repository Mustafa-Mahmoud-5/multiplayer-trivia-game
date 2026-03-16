package org.game.server.repositories;

import org.game.common.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO(Abdullah): implement the repo, we have GSON library installed, check pom.xml
public class UserRepo {
    List<User> users = new ArrayList<>();

    public void load(String users) {
        // extract json, store in memory
    }

    public List<User> findAll() {
        return users;
    }

    public User getByUserName(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
    }
}
