package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Duration;
import java.time.Instant;

public final class RefreshToken {
    private final String value;
    private final Instant expiresAt;
    private RefreshToken(final String value, final Instant expiresAt) {
        this.value =  value;
        this.expiresAt = expiresAt;
    }
    public static Result<RefreshToken> createWithTtl(final String value, final Duration ttl, final Instant issueAt) {
        if (value == null || value.isBlank()) {
            return Result.failure("refresh token value must not be blank", ErrorType.INVARIANT_VIOLATION);
        }
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            return Result.failure("refresh token TTL must be positive", ErrorType.INVARIANT_VIOLATION);
        }
        if (issueAt == null) {
            return Result.failure("issueAt must not be null", ErrorType.INVARIANT_VIOLATION);
        }

        final Instant exp = issueAt.plus(ttl);
        return Result.success(new RefreshToken(value, exp));
    }

    public String value() {
        return value;
    }

    public Instant expiresAt() {
        return expiresAt;
    }
    public boolean isExpired(final Instant now) {
        return !expiresAt.isAfter(now);
    }

    public long secondsUntilExpiration(final Instant now) {
        return Duration.between(now, expiresAt).getSeconds();
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

