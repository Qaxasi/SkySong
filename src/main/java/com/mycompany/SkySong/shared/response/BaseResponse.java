package com.mycompany.SkySong.shared.response;

public abstract class BaseResponse {
    private final String message;

    protected BaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
