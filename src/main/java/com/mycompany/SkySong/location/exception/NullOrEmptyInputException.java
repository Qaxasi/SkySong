package com.mycompany.SkySong.location.exception;

public class NullOrEmptyInputException extends RuntimeException {
    public LocationNotGiven(String message) {
        super(message);
    }
    public LocationNotGiven(String message, Throwable cause) {
        super(message, cause);
    }
}
