package com.mycompany.SkySong.registration.infrastructure.exception;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class CredentialValidationException extends ApplicationRuntimeException {
    public CredentialValidationException(String message) {
        super(message);
    }
}
