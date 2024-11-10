package com.mycompany.SkySong.adapter.exception.common;

public class ApiServerErrorException extends RuntimeException {
    public ApiServerErrorException(String message) {
        super(message);
    }
    public ApiServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
