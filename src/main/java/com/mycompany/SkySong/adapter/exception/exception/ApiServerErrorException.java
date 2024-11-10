package com.mycompany.SkySong.adapter.exception.exception;

public class ApiServerErrorException extends RuntimeException {
    public ApiServerErrorException(String message) {
        super(message);
    }
    public ApiServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
