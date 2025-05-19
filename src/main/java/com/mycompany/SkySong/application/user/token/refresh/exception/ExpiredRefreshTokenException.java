package com.mycompany.SkySong.application.user.token.refresh.exception;

import com.mycompany.SkySong.application.shared.exception.ApplicationException;

public class ExpiredRefreshTokenException extends ApplicationException {
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}
