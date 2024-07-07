package com.mycompany.SkySong.security.exception;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class SessionNotFoundException extends ApplicationRuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
