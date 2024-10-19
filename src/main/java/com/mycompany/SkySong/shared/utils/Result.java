package com.mycompany.SkySong.shared.utils;

import java.util.function.Function;

public record Result<T>(T data, String errorMessage, boolean success) {

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        if (this.success()) {
            return mapper.apply(this.data);
        } else {
            return Result.failure(this.errorMessage());
        }
    }
}
