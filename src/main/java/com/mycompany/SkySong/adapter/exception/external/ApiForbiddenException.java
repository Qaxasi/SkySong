package com.mycompany.SkySong.adapter.exception.external;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class ApiForbiddenException extends BaseApiException {
    public ApiForbiddenException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
