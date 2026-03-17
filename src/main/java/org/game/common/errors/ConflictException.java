package org.game.common.errors;

public class ConflictException extends CustomException {
    public ConflictException(String message) {
        super(message, 429);
    }
}
