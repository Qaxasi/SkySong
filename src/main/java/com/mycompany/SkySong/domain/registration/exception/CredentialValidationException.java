package com.mycompany.SkySong.domain.registration.exception;

public class CredentialValidationException extends RuntimeException {
    public CredentialValidationException(String message) {
        super(message);
    }
}
