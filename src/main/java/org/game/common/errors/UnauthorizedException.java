package org.game.common.errors;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}