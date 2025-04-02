package com.mycompany.SkySong.adapter.exception.common;

public class ApiRequestTimeoutException extends RuntimeException {
    public ApiRequestTimeoutException(String message) {
        super(message);
    }
    public ApiRequestTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
