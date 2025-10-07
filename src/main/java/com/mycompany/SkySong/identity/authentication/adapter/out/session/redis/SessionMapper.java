package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.shared.domain.UserId;
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
                session.authzVersionAtIssue());
    }

    static Result<Session> toDomain(final SessionEntry entry) {
        final Instant issued;
        final Instant expires;

        try {
            issued = Instant.ofEpochSecond(entry.issuedAt());
            expires = Instant.ofEpochSecond(entry.expiresAt());
        } catch (DateTimeException | ArithmeticException ex)  {
            return Result.failure("Invalid epoch seconds in cache payload", ErrorType.PERSISTENCE_ERROR);
        }

        return UserId.of(entry.userId())
                .flatMap(id -> Session.create(id,
                        issued,
                        expires,
                        entry.authzVersionAtIssue()));
    }
}
