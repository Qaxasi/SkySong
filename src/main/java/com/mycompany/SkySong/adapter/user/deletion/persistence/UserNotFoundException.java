package com.mycompany.SkySong.adapter.user.deletion.persistence;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
