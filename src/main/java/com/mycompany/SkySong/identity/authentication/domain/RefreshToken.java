package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class RefreshToken {
    private final String value;
    private final Instant expiresAt;
    private RefreshToken(final String value, final Instant expiresAt) {
        this.value = Objects.requireNonNull(value, "refresh token value must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expires at must not be null");
    }
    public static Result<RefreshToken> of(final String value, final Instant expiresAt) {
        if (value == null || value.isBlank()) {
            return Result.failure("refresh token value must not be blank", ErrorType.INVARIANT_VIOLATION);
        }
        if (expiresAt == null) {
            return Result.failure("expires at must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new RefreshToken(value, expiresAt));
    }

    public String value() {
        return value;
    }

    public Instant expiresAt() {
        return expiresAt;
    }
    public boolean isExpired(final Instant referenceTime) {
        return !expiresAt.isAfter(referenceTime);
    }

    public Duration remainingTtl(final Instant referenceTime) {
        return Duration.between(referenceTime, expiresAt);
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
