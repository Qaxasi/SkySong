package com.mycompany.SkySong.login.domain.exception;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class UserNotFoundException extends ApplicationRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
