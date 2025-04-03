package com.mycompany.SkySong.adapter.exception.external;

import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.error.ErrorType;

public class ApiClientErrorException extends BaseApiException {
    public ApiClientErrorException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
