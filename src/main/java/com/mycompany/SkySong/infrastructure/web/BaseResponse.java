package com.mycompany.SkySong.infrastructure.http.response;

public abstract class BaseResponse {
    private final String message;

    protected BaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
