package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Session(
        UserId userId,
        String username,
        Set<String> roles,
        Instant issuedAt,
        Instant expiresAt,
        long sessionVersionAtIssue) {

    public Session {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be null");
        }
        if (issuedAt == null) {
            throw new IllegalArgumentException("issuedAt must not be null");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("expiresAt must not be null");
        }
        if (!expiresAt.isAfter(issuedAt)) {
            throw new IllegalArgumentException("expiresAt must be after issuedAt");
        }
        if (sessionVersionAtIssue < 0) {
            throw new IllegalArgumentException("session version must be non-negative");
        }

        username = username.strip();
        roles = (roles == null)
                ? Set.of()
                : roles.stream()
                .filter(Objects::nonNull)
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
    }
    public static Result<Session> create(final UserId userId, final String username, final Set<String> roles,
                                         final Instant issuedAt, final Instant expiresAt, final long sessionVersionAtIssue) {
        try {
            return Result.success(new Session(userId, username, roles, issuedAt, expiresAt, sessionVersionAtIssue));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ex.getMessage(), ErrorType.INVARIANT_VIOLATION);
        }
    }

    public boolean isExpired(final Instant now) {
        return !expiresAt.isAfter(now);
    }
    public long remainingTtlSeconds(final Instant now) {
        final long s = Duration.between(now, expiresAt).getSeconds();
        return Math.max(s, 0L);
    }
}
