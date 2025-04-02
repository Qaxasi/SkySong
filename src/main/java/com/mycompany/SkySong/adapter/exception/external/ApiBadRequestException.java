package com.mycompany.SkySong.adapter.exception.external;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class ApiBadRequestException extends BaseApiException {
    public ApiBadRequestException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
