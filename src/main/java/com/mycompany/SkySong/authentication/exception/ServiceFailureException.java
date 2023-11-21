package com.mycompany.SkySong.authentication.exception;

public class ServiceFailureException extends ApplicationRuntimeException {
    public ServiceFailureException(String message) {
        super(message);
    }
}
