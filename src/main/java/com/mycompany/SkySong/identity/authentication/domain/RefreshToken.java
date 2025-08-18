package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public record RefreshToken(String value, Instant expiresAt) {
    public static Result<RefreshToken> of(final String value, final Instant expiresAt) {
        if (value == null || value.isBlank()) {
            return Result.failure("refresh token value must not be null or blank", ErrorType.INVARIANT_VIOLATION);
        }
        if (expiresAt == null) {
            return Result.failure("expiresAt must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new RefreshToken(value, expiresAt));
    }

    public boolean isExpired(final Clock clock) {
        return !expiresAt.isAfter(Instant.now(clock));
    }

    public long ttlSeconds(final Clock clock) {
        long s = Duration.between(Instant.now(clock), expiresAt).getSeconds();
        return Math.max(s, 0L);
    }
}
