package com.mycompany.SkySong.auth.exception;

public class RoleNotFoundException extends ApplicationRuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
