package com.mycompany.SkySong.application.user.token.refresh.exception;

import com.mycompany.SkySong.application.shared.exception.ApplicationException;

public class InvalidRefreshTokenException extends ApplicationException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
