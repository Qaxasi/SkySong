package com.mycompany.SkySong.adapter.exception.common;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
