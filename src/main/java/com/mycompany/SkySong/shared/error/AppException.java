package com.mycompany.SkySong.shared.error;

public class AppException extends RuntimeException {
    private final ErrorType errorType;
    public AppException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
    public AppException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
