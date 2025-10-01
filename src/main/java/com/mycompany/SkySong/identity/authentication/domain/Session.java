package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class Session {
    private final UserId userId;
    private final String username;
    private final Set<String> roles;
    private final Instant issuedAt;     
    private final Instant expiresAt;
    private final long sessionVersionAtIssue;

    private Session(UserId userId, String username, Set<String> roles, Instant issuedAt, Instant expiresAt, long sessionVersionAtIssue) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.sessionVersionAtIssue = sessionVersionAtIssue;
    }

    public static Result<Session> create(final UserId userId, final String username, final Set<String> roles,
                                         final Instant issuedAt, final Instant expiresAt, final long sessionVersionAtIssue) {
        if (userId == null) {
            return Result.failure("userId must not be null", ErrorType.INVARIANT_VIOLATION);
        }
        if (username == null || username.isBlank()) {
            return Result.failure("username must not be blank", ErrorType.INVARIANT_VIOLATION);
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
        if (sessionVersionAtIssue < 0) {
            return Result.failure("session version must be non-negative", ErrorType.INVARIANT_VIOLATION);
        }

        final String u = username.strip();
        final Set<String> safeRoles = (roles == null)
                ? Set.of()
                : roles.stream()
                .filter(Objects::nonNull)
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableSet());

        return Result.success(new Session(userId, u, safeRoles, issuedAt, expiresAt, sessionVersionAtIssue));
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

    public String username() {
        return username;
    }

    public Set<String> roles() {
        return roles;
    }

    public Instant issuedAt() {
        return issuedAt;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public long sessionVersionAtIssue() {
        return sessionVersionAtIssue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Session) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.roles, that.roles) &&
                Objects.equals(this.issuedAt, that.issuedAt) &&
                Objects.equals(this.expiresAt, that.expiresAt) &&
                this.sessionVersionAtIssue == that.sessionVersionAtIssue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, roles, issuedAt, expiresAt, sessionVersionAtIssue);
    }

    @Override
    public String toString() {
        return "Session[" +
                "userId=" + userId + ", " +
                "username=" + username + ", " +
                "roles=" + roles + ", " +
                "issuedAt=" + issuedAt + ", " +
                "expiresAt=" + expiresAt + ", " +
                "sessionVersionAtIssue=" + sessionVersionAtIssue + ']';
    }
}
