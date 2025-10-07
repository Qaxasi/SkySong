package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Duration;
import java.time.Instant;

public final class Session {
    private final UserId userId;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private final long authzVersionAtIssue;

    private Session(UserId userId, Instant issuedAt, Instant expiresAt, long authzVersionAtIssue) {
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.authzVersionAtIssue = authzVersionAtIssue;
    }

    public static Result<Session> create(final UserId userId, final Instant issuedAt,
                                         final Instant expiresAt, final long authzVersionAtIssue) {
        if (userId == null) {
            return Result.failure("userId must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (issuedAt == null) {
            return Result.failure("issuedAt must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (expiresAt == null) {
            return Result.failure("expiresAt must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (!expiresAt.isAfter(issuedAt)) {
            return Result.failure("expiresAt must be after issuedAt", ErrorType.INVARIANT_VIOLATION);
        }
        if (authzVersionAtIssue < 0) {
            return Result.failure("authz version must be non-negative", ErrorType.INVARIANT_VIOLATION);
        }

        return Result.success(new Session(userId, issuedAt, expiresAt, authzVersionAtIssue));
    }

    public boolean isExpired(final Instant now) {
        return !expiresAt.isAfter(now);
    }

    public long remainingTtlSeconds(final Instant now) {
        final long s = Duration.between(now, expiresAt).getSeconds();
        return Math.max(s, 0L);
    }

    public UserId userId() {
        return userId;
    }
    public Instant issuedAt() {
        return issuedAt;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public long authzVersionAtIssue() {
        return authzVersionAtIssue;
    }

    @Override
    public String toString() {
        return "Session{" +
                "userId=" + userId +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                ", authzVersionAtIssue=" + authzVersionAtIssue +
                '}';
    }
}
