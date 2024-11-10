package com.mycompany.SkySong.adapter.exception.exception;

public class ApiClientErrorException extends RuntimeException {
    public ApiClientErrorException(String message) {
        super(message);
    }
    public ApiClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
