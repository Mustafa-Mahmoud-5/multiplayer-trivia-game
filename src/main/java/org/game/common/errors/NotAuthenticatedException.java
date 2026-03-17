package org.game.common.errors;

public class NotAuthenticatedException extends CustomException {
    public NotAuthenticatedException(String message) {
        super(message, 403);
    }
}