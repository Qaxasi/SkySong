package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class UsernameAlreadyExist extends BaseApiException {
    public UsernameAlreadyExist(String message) {
        super(message);
    }
}
