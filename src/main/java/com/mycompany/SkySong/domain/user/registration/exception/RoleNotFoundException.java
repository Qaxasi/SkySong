package com.mycompany.SkySong.domain.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class RoleNotFoundException extends BaseApiException {
    public RoleNotFoundException(String message) {
        super(message, ErrorType.ROLE_NOT_FOUND);
    }
}
