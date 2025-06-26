package com.mycompany.SkySong.identity.application.authentication.login.exception;

import com.mycompany.SkySong.identity.application.exception.IdentityApplicationException;

public class InvalidCredentialsException extends IdentityApplicationException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
