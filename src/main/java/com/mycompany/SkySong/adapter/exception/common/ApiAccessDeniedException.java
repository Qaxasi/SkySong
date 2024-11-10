package com.mycompany.SkySong.adapter.exception.common;

public class ApiAccessDeniedException extends RuntimeException {
    public ApiAccessDeniedException(String message) {
        super(message);
    }
    public ApiAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
