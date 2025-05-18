package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.domain.shared.exception.DomainException;

public class InvalidUsernameFormat extends DomainException {
    public InvalidUsernameFormat(String message) {
        super(message);
    }
}
