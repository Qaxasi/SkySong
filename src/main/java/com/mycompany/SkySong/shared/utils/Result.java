package com.mycompany.SkySong.shared.utils;

public record Result<T>(T data, String errorMessage, boolean success) {

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }
}
