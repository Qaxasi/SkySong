package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class Session {
    private final UserId userId;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private final AccessVersion accessVersionAtIssue;

    private Session(final UserId userId, final Instant issuedAt,
                    final Instant expiresAt, final AccessVersion accessVersionAtIssue) {
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.accessVersionAtIssue = accessVersionAtIssue;
    }

    public static Result<Session> fromStored(final UserId userId, final Instant issuedAt,
                                             final Instant expiresAt, final AccessVersion accessVersion) {
        if (userId == null) {
            return Result.failure("user id must not be null", ErrorType.DATA_INTEGRITY_ERROR);
        }
        if (issuedAt == null) {
            return Result.failure("issued at must not be null", ErrorType.DATA_INTEGRITY_ERROR);
        }
        if (expiresAt == null) {
            return Result.failure("expires at must not be null", ErrorType.DATA_INTEGRITY_ERROR);
        }
        if (accessVersion == null) {
            return Result.failure("access version must not be null", ErrorType.DATA_INTEGRITY_ERROR);
        }
        if (!expiresAt.isAfter(issuedAt)) {
            return Result.failure("expires at must be after issued at", ErrorType.DATA_INTEGRITY_ERROR);
        }

        return Result.success(new Session(userId, issuedAt, expiresAt, accessVersion));
    }

    public static Result<Session> issue(final UserId userId, final Instant issuedAt,
                                        final Instant expiresAt, final AccessVersion accessVersion) {
        if (userId == null) {
            return Result.failure("user id must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (issuedAt == null) {
            return Result.failure("issued at must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (expiresAt == null) {
            return Result.failure("expires at must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (!expiresAt.isAfter(issuedAt)) {
            return Result.failure("expires at must be after issued at", ErrorType.INVARIANT_VIOLATION);
        }
        if (accessVersion == null) {
            return Result.failure("access version must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new Session(userId, issuedAt, expiresAt, accessVersion));
    }

    public Result<Session> reissue(final Instant reissueAt, final Duration ttl,
                                   final AccessVersion currentAccessVersion) {
        if (reissueAt == null) {
            return Result.failure("reissue at must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            return Result.failure("ttl must be positive", ErrorType.INVARIANT_VIOLATION);
        }
        if (currentAccessVersion == null) {
            return Result.failure("access version must not be null", ErrorType.INVARIANT_VIOLATION);
        }

        final Instant newExpiresAt = reissueAt.plus(ttl);
        return issue(userId, reissueAt, newExpiresAt, currentAccessVersion);
    }

    public boolean isExpired(final Instant referenceTime) {
        return !expiresAt.isAfter(referenceTime);
    }
    public boolean isAccessVersionOutdated(final AccessVersion current) {
        return !this.accessVersionAtIssue.isSameAs(current);
    }

    public long secondsUntilExpiration(final Instant referenceTime) {
        return Duration.between(referenceTime, expiresAt).getSeconds();
    }

    public Duration ttl(final Instant now) {
        return Duration.between(now, expiresAt);
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

    public AccessVersion accessVersionAtIssue() {
        return accessVersionAtIssue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, issuedAt, expiresAt, accessVersionAtIssue);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Session s)) return false;
        return Objects.equals(userId, s.userId)
                && Objects.equals(issuedAt, s.issuedAt)
                && Objects.equals(expiresAt, s.expiresAt)
                && Objects.equals(accessVersionAtIssue, s.accessVersionAtIssue);
    }


    @Override
    public String toString() {
        return "Session{" +
                "userId=" + userId +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                ", accessVersionAtIssue=" + accessVersionAtIssue +
                '}';
    }
}
