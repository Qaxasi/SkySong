package com.mycompany.SkySong.shared.exception;

public class NullOrEmptyInputException extends ApplicationRuntimeException {
    public NullOrEmptyInputException(String message) {
        super(message);
    }
}