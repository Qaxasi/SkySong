package com.mycompany.SkySong.domain.user.registration.exception;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class InvalidEmailFormat extends BaseApiException {
    public InvalidEmailFormat(String message) {
        super(message);
    }
}
