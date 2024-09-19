package com.mycompany.SkySong.adapter.geocoding.exception;

public class WebClientTimeoutException extends RuntimeException {
    public WebClientTimeoutException(String message) {
        super(message);
    }
    public WebClientTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
