package org.game.common.errors;

import org.game.common.errors.CustomException;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message, 429);
    }
}
