package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class InvalidUsernameFormat extends BaseApiException {
    public InvalidUsernameFormat(String message) {
        super(message);
    }
}
