package org.game.common.errors;

public class InvalidArgumentException extends CustomException {
    public InvalidArgumentException(String message) {
        super(message, 400);
    }
}