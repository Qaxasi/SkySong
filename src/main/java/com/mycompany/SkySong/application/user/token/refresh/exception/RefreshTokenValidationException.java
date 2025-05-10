package com.mycompany.SkySong.application.user.token.refresh.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class RefreshTokenValidationException extends BaseApiException {
    public RefreshTokenValidationException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
