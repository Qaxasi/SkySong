package com.mycompany.SkySong.shared.error;

public class BaseApiException extends RuntimeException {
    public BaseApiException(String message) {
        super(message);
    }
}
