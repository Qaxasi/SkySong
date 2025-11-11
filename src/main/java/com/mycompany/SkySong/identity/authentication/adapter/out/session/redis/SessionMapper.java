package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.domain.AccessVersion;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.domain.UserId;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.DateTimeException;
import java.time.Instant;

class SessionMapper {
    private SessionMapper() {}
    static SessionEntry toDto(final Session session) {
        return new SessionEntry(
                session.userId().asInt(),
                session.issuedAt().getEpochSecond(),
                session.expiresAt().getEpochSecond(),
                session.accessVersionAtIssue().asInt());
    }

    static Result<Session> toDomain(final SessionEntry entry) {
        return Result.combineM(
                UserId.restore(entry.userId()),
                epochToInstant(entry.issuedAt()),
                epochToInstant(entry.expiresAt()),
                AccessVersion.restore(entry.accessVersionAtIssue()),
                Session::fromStored
                );
    }

    private static Result<Instant> epochToInstant(final long epochSeconds) {
        try {
            return Result.success(Instant.ofEpochSecond(epochSeconds));
        } catch (DateTimeException | ArithmeticException ex) {
            return Result.failure("Invalid epoch seconds in cache payload", ErrorType.DATA_INTEGRITY_ERROR);
        }
    }
}
