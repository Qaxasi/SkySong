package com.mycompany.SkySong.identity.application.registration.exception;

import com.mycompany.SkySong.identity.application.exception.IdentityApplicationException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class UserRegistrationValidationException extends IdentityApplicationException {
    public UserRegistrationValidationException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
