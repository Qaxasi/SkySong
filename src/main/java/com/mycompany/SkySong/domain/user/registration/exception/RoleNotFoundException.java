package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class RoleNotFoundException extends BaseApiException {
    public RoleNotFoundException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
