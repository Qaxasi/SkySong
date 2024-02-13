package com.mycompany.SkySong.shared.exception;

public class SessionNotFoundException extends ApplicationRuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
