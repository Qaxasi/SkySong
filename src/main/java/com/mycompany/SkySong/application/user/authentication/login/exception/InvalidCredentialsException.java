package com.mycompany.SkySong.application.user.authentication.login.exception;

import com.mycompany.SkySong.application.shared.exception.ApplicationException;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
