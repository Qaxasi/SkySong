package com.mycompany.SkySong.adapter.exception.common;

public class ApiAuthorizationException extends RuntimeException {
    public ApiAuthorizationException(String message) {
        super(message);
    }
    public ApiAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
