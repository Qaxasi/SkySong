package com.mycompany.SkySong.shared.result;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.response.ErrorResponse;

import java.util.function.Function;

public record Result<T>(
        T data,
        String errorMessage,
        boolean isSuccessful,
        ErrorType errorType) {

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true, null);
    }

    public static Result<Void> success() {
        return new Result<>(null, null, true, null);
    }

    public static <T> Result<T> failure(String errorMessage, ErrorType errorType) {
        return new Result<>(null, errorMessage, false, errorType);
    }

    public boolean isSuccess() {
        return isSuccessful;
    }

    public boolean isFailure() {
        return !isSuccessful;
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        if (this.isSuccess()) {
            return mapper.apply(this.data);
        } else {
            return Result.failure(this.errorMessage(), this.errorType());
        }
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        if (this.isSuccess()) {
            return Result.success(mapper.apply(this.data));
        } else {
            return Result.failure(this.errorMessage(), this.errorType());
        }
    }

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(errorMessage, errorType.name(), errorType.getHttpStatus().value());
    }
}