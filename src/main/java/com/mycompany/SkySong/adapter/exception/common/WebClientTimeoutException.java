package com.mycompany.SkySong.adapter.exception.common;

public class WebClientTimeoutException extends RuntimeException {
    public WebClientTimeoutException(String message) {
        super(message);
    }
    public WebClientTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
