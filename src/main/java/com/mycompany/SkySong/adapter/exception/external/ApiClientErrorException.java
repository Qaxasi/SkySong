package com.mycompany.SkySong.adapter.exception.external;

import com.mycompany.SkySong.shared.error.BaseApiException;

public class ApiUnexpectedClientException extends BaseApiException {
    public ApiUnexpectedClientException(String message) {
        super(message);
    }
}
