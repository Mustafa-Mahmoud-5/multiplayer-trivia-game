package org.game.server.services;

import org.game.common.errors.ConflictException;
import org.game.common.errors.NotFoundException;
import org.game.common.models.User;
import org.game.common.protocol.MessageParser;
import org.game.common.protocol.enums.MessageType;
import org.game.server.repositories.UserRepo;

public class AuthService {
    UserRepo userRepo;
    public AuthService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void login(String name, String pass) {
        User user = userRepo.getByUserName(name);
        if(user== null) {
            throw new NotFoundException("User not found");
        };

        if(!user.getPassword().equals(pass)) {
            throw new NotFoundException("invalid username or password");
        }

        user.setLoggedIn(true);
    }

    public User register(String name, String pass) {
        User user = userRepo.getByUserName(name);
        if(user != null) {
            throw new ConflictException("Username taken");
        };

        User newUser = new User(name, pass, "user");
        newUser.setLoggedIn(true);
        userRepo.addUser(newUser);

        return newUser;
    }
}
