package com.mycompany.SkySong.adapter.user.login.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class InvalidCredentialsException extends BaseApiException {
    public InvalidCredentialsException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
