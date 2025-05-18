package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class InvalidPasswordFormat extends BaseApiException {
    public InvalidPasswordFormat(String message) {
        super(message);
    }
}
