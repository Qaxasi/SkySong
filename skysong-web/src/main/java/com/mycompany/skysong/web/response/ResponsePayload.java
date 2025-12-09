package com.mycompany.skysong.web.response;

import com.mycompany.skysong.web.error.ErrorResponse;

import java.util.Objects;

public sealed interface ResponsePayload<T>
        permits ResponsePayload.Success, ResponsePayload.Error {

    record Success<T>(T data) implements ResponsePayload<T> {
        public Success {
            Objects.requireNonNull(data, "data must not be null");
        }
    }

    record Error<T>(ErrorResponse errorResponse) implements ResponsePayload<T> {
        public Error {
            Objects.requireNonNull(errorResponse, "errorResponse must not be null");
        }
    }

    static <T> ResponsePayload<T> ok(final T data) {
        return new Success<>(data);
    }

    static <T> ResponsePayload<T> error(final ErrorResponse errorResponse) {
        return new Error<>(errorResponse);
    }
}
