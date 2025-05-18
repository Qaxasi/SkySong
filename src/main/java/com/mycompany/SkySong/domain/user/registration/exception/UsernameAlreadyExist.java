package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.domain.shared.exception.DomainException;

public class UsernameAlreadyExist extends DomainException {
    public UsernameAlreadyExist(String message) {
        super(message);
    }
}
