package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record Session(
        int userId,
        String username,
        List<String> roles,
        Instant issueAt,
        Instant refreshTokenExpiresAt) {

    public static Result<Session> create(final int userId, final String username, final List<String> roles,
                                         final Instant issueAt, final Instant refreshTokenExpiresAt) {
        if (userId <= 0) {
            return Result.failure("user id must be positive", ErrorType.INVARIANT_VIOLATION);
        }

        if (issueAt == null || refreshTokenExpiresAt == null) {
            return Result.failure("issueAt/refreshTokenExpiresAt cannot be null", ErrorType.INVARIANT_VIOLATION);
        }

        if (!refreshTokenExpiresAt.isAfter(issueAt)) {
            return Result.failure("non-positive TTL", ErrorType.INVARIANT_VIOLATION);
        }

        if (username == null) {
            return Result.failure("username cannot be null", ErrorType.INVARIANT_VIOLATION);
        }

        final String u = username.strip();
        if (u.isEmpty()) {
            return Result.failure("username cannot be blank", ErrorType.INVARIANT_VIOLATION);
        }

        final List<String> safeRoles = (roles == null)
                ? List.of()
                : roles.stream()
                .filter(Objects::nonNull)
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();

        return Result.success(new Session(userId, u, List.copyOf(safeRoles), issueAt, refreshTokenExpiresAt));

    }

    public boolean isRefreshTokenExpired(final Instant now) {
        return !refreshTokenExpiresAt.isAfter(now);
    }

    public Duration remainingTtl(final Clock clock) {
        final Instant now = Instant.now(clock);
        if (!refreshTokenExpiresAt.isAfter(now)) {
            return Duration.ZERO;
        }
        return Duration.between(now, refreshTokenExpiresAt);
    }

    public long remainingTtlSeconds(final Clock clock) {
        return Math.max(remainingTtl(clock).getSeconds(), 0L);
    }
}
