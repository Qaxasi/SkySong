package com.mycompany.SkySong.infrastructure.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends BaseResponse {
    private final String errorCode;
    private final int status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, String> errors;

    public ErrorResponse(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.errors = null;
    }

    public ErrorResponse(Map<String, String> errors, String errorCode, int status) {
        super(null);
        this.errorCode = errorCode;
        this.status = status;
        this.errors = errors == null || errors.isEmpty() ? null : Map.copyOf(errors);
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
