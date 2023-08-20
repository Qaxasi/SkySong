package com.mycompany.SkySong.exception;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException(String message) {
        super(message);
    }
    public LocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
