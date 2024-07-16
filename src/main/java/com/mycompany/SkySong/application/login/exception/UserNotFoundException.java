package com.mycompany.SkySong.domain.login.exception;

import com.mycompany.SkySong.adapter.exception.ApplicationRuntimeException;

public class UserNotFoundException extends ApplicationRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
