package com.mycompany.SkySong.identity.application.exception;

import com.mycompany.SkySong.shared.error.ErrorType;

public class IdentityApplicationException extends RuntimeException {
    private final ErrorType errorType;
    public IdentityApplicationException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public IdentityApplicationException(String message) {
        super(message);
        this.errorType = null;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
