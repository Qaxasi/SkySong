package com.mycompany.SkySong.adapter.exception.common;

public class ApiTooManyRequestsException extends RuntimeException {
    
    public ApiTooManyRequestsException(String message) {
        super(message);
    }
    public ApiTooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
    }
}
