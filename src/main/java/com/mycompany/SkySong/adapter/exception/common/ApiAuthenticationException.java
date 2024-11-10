package com.mycompany.SkySong.adapter.exception.common;

public class ApiAuthenticationException extends RuntimeException {
    public ApiAuthenticationException(String message) {
        super(message);
    }
    public ApiAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
