package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.domain.shared.exception.DomainException;

public class InvalidPasswordFormat extends DomainException {
    public InvalidPasswordFormat(String message) {
        super(message);
    }
}
