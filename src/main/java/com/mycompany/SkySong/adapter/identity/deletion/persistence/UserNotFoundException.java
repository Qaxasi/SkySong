package com.mycompany.SkySong.adapter.identity.deletion.persistence.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
