package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class RoleNotFoundException extends BaseApiException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
