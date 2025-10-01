package com.mycompany.SkySong.infrastructure.http;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public record ErrorView(ErrorType errorType,
                        String message) {
    public static ErrorView fromResult(Result<?> failure) {
        return new ErrorView(
                failure.errorType(),
                failure.errorMessage());
    }
}
