package com.mycompany.SkySong.authentication.exception;

public class ServiceFailureException extends RuntimeException {
    public ServiceFailureException(String message) {
        super(message);
    }
}
