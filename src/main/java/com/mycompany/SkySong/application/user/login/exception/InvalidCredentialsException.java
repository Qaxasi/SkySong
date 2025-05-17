package com.mycompany.SkySong.application.user.login.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class InvalidCredentialsException extends BaseApiException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
