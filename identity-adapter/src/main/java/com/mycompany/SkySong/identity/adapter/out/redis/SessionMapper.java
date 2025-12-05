package com.mycompany.SkySong.identity.adapter.out.redis;

import com.mycompany.skysong.identity.domain.Session;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.time.DateTimeException;
import java.time.Instant;

class SessionMapper {
    private SessionMapper() {}
    static SessionEntry toDto(final Session session) {
        return new SessionEntry(
                session.userId().asInt(),
                session.issuedAt().getEpochSecond(),
                session.expiresAt().getEpochSecond());
    }

    static Result<Session> toDomain(final SessionEntry entry) {
        return Result.combineM(
                UserId.fromStored(entry.userId()),
                epochToInstant(entry.issuedAt()),
                epochToInstant(entry.expiresAt()),

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
