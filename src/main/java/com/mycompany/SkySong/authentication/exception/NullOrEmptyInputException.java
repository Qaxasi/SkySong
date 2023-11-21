package com.mycompany.SkySong.authentication.exception;

public class NullOrEmptyInputException extends ApplicationRuntimeException {
    public NullOrEmptyInputException(String message) {
        super(message);
    }
}