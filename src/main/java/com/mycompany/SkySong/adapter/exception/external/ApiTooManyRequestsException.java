package com.mycompany.SkySong.adapter.exception.external;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class ApiTooManyRequestsException extends BaseApiException {
    public ApiTooManyRequestsException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
