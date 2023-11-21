package com.mycompany.SkySong.authentication.exception;

public class UserNotFoundException extends ApplicationRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
