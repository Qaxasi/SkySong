package com.mycompany.SkySong.shared.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final String message;
    private final String errorCode;
    private final int status;
    private final Map<String, String> errors;

    public ErrorResponse(String message, String errorCode, int status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.errors = null;
    }

    public ErrorResponse(Map<String, String> errors, String errorCode, int status) {
        this.message = null;
        this.errorCode = errorCode;
        this.status = status;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
