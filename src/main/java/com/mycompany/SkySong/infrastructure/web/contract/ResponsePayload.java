package com.mycompany.SkySong.infrastructure.web.contract;

public record ResponsePayload<T>(T data, ErrorResponse errorResponse) {
    public static <T> ResponsePayload<T> ok(T data) {
        return new ResponsePayload<>(data, null);
    }
    public static <T> ResponsePayload<T> error(ErrorResponse errorResponse) {
        return new ResponsePayload<>(null, errorResponse);
    }
}
