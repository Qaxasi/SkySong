package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class EmailAlreadyExist extends BaseApiException {
    public EmailAlreadyExist(String message) {
        super(message);
    }
}
