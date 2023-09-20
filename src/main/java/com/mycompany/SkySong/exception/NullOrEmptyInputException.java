package com.mycompany.SkySong.exception;

public class NullOrEmptyInputException extends RuntimeException {
    public NullOrEmptyInputException(String message) {
        super(message);
    }
}
