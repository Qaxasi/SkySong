package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.domain.shared.exception.DomainException;

public class EmailAlreadyExist extends DomainException {
    public EmailAlreadyExist(String message) {
        super(message);
    }
}
