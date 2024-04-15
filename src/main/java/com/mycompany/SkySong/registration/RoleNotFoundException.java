package com.mycompany.SkySong.registration;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class RoleNotFoundException extends ApplicationRuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
