package com.mycompany.SkySong.auth.exception;

public class SessionNotFoundException extends ApplicationRuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
