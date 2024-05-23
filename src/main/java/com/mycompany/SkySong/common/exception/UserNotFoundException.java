package com.mycompany.SkySong.common.exception;

public class UserNotFoundException extends ApplicationRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
