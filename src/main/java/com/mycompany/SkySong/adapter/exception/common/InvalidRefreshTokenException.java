package com.mycompany.SkySong.adapter.exception.common;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class InvalidRefreshTokenException extends BaseApiException {
    public InvalidRefreshTokenException(String message) {
        super(message, ErrorType.INVALID_REFRESH_TOKEN);
    }
}
