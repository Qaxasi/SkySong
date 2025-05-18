package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.domain.shared.exception.DomainException;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
