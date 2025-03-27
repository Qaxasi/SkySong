package com.mycompany.SkySong.shared.error;

public class BaseApiException extends RuntimeException {

    private final ErrorType errorType;

    public BaseApiException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
