package com.mycompany.SkySong.adapter.exception.common;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
