package com.mycompany.SkySong.identity.application.registration.exception;

import com.mycompany.SkySong.identity.application.exception.IdentityApplicationException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class UserRoleConfigurationException extends IdentityApplicationException {
    public UserRoleConfigurationException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
