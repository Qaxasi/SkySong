package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class CredentialValidationException extends BaseApiException {
    public CredentialValidationException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
