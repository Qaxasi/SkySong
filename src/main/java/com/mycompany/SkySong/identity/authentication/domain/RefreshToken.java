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
    public static Result<RefreshToken> createWithTtl(final String value, final Duration ttl, final Instant now) {
        if (value == null || value.isBlank()) {
            return Result.failure("refresh token value is null or blank", ErrorType.INVARIANT_VIOLATION);
        }
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            return Result.failure("invalid refresh token ttl", ErrorType.INVARIANT_VIOLATION);
        }
        if (now == null) {
            return Result.failure("now is null", ErrorType.INVARIANT_VIOLATION);
        }

        final Instant exp = now.plus(ttl);
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

    public long ttlSeconds(final Instant now) {
        long s = Duration.between(now, expiresAt).getSeconds();
        return Math.max(s, 0L);
    }

    @Override
    public String toString() {
        return "Refresh token(*****)";
    }
}
