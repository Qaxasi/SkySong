package com.mycompany.SkySong.shared.result;

import com.mycompany.SkySong.shared.utils.ErrorType;

import java.util.function.Function;

public record Result<T>(T data, String errorMessage, boolean success, ErrorType errorType) {

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true, null);
    }

    public static <T> Result<T> failure(String errorMessage, ErrorType errorType) {
        return new Result<>(null, errorMessage, false, errorType);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        if (this.success()) {
            return mapper.apply(this.data);
        } else {
            return Result.failure(this.errorMessage(), this.errorType());
        }
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        if (this.success()) {
            return Result.success(mapper.apply(this.data));
        } else {
            return Result.failure(this.errorMessage(), this.errorType());
        }
    }
}
