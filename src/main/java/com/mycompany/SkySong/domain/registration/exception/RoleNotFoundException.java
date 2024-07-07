package com.mycompany.SkySong.registration.domain.exception;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class RoleNotFoundException extends ApplicationRuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
