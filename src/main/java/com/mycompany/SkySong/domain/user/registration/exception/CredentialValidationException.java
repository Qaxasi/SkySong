package com.mycompany.SkySong.domain.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class CredentialValidationException extends BaseApiException {
    public CredentialValidationException(String message) {
        super(message, ErrorType.INVALID_CREDENTIALS);
    }
}
