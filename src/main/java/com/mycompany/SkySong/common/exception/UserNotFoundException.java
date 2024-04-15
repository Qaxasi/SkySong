package com.mycompany.SkySong.common.exception;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class UserNotFoundException extends ApplicationRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
