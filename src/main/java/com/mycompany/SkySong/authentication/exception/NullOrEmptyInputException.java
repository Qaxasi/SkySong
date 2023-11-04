package com.mycompany.SkySong.authentication.exception;

public class NullOrEmptyInputException extends RuntimeException {
    public NullOrEmptyInputException(String message) {
        super(message);
    }
}