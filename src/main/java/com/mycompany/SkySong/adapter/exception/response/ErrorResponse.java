package com.mycompany.SkySong.adapter.exception.response;

import java.util.Map;

public class ErrorResponse {
    private String error;
    private Map<String, String> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
