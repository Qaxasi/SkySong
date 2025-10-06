package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.identity.shared.domain.UserRole;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

class SessionMapper {
    private SessionMapper() {}
    static SessionEntry toDto(final Session session) {
        final Set<String> roles = session.roles()
                .stream()
                .map(UserRole::code)
                .collect(Collectors.toUnmodifiableSet());

        return new SessionEntry(
                session.userId().asInt(),
                session.username(),
                roles,
                session.issuedAt().getEpochSecond(),
                session.expiresAt().getEpochSecond(),
                session.sessionVersionAtIssue());
    }

    static Result<Session> toDomain(final SessionEntry entry) {
        final EnumSet<UserRole> tmpRoles = EnumSet.noneOf(UserRole.class);
        if (entry.roles() != null) {
            for (String r : entry.roles()) {
                if (r == null || r.isBlank()) {
                    continue;
                }
                try {
                    tmpRoles.add(UserRole.valueOf(r));
                } catch (IllegalArgumentException ex) {
                    return Result.failure("Unsupported role in cache payload: " + r, ErrorType.PERSISTENCE_ERROR);
                }
            }
        }

        final Set<UserRole> roles = Set.copyOf(tmpRoles);

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
                        entry.username(),
                        roles,
                        issued,
                        expires,
                        entry.sessionVersionAtIssue()));
    }
}
