package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Objects;

public final class RefreshToken {
    private final String value;

    private RefreshToken(final String value) {
        this.value = Objects.requireNonNull(value, "refresh token value must not be null");
    }

    public static Result<RefreshToken> fromInput(final String value) {
        return validate(value, ErrorType.VALIDATION_ERROR);
    }

    public static Result<RefreshToken> ofGenerated(final String value) {
        return validate(value, ErrorType.INVARIANT_VIOLATION);
    }

    private static Result<RefreshToken> validate(final String value, final ErrorType errorType) {
        if (value == null || value.isBlank()) {
            return Result.failure("refresh token value must not be blank", errorType);
        }
        return Result.success(new RefreshToken(value));
    }

    public String value() {
        return value;
    }
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof RefreshToken rt && value.equals(rt.value));
    }
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    @Override
    public String toString() {
        return "Refresh token(*****)";
    }
}
