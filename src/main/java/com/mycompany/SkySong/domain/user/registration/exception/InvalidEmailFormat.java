package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.domain.shared.exception.DomainException;

public class InvalidEmailFormat extends DomainException {
    public InvalidEmailFormat(String message) {
        super(message);
    }
}
