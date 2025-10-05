package com.mycompany.SkySong.identity.authentication.application.shared.mapper;

import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.shared.domain.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

public class AccessTokenClaimsMapper {
    private AccessTokenClaimsMapper() {}

    public static AccessTokenClaims from(final Session session) {
        final Set<String> roles = session.roles().stream()
                .map(UserRole::name)
                .collect(Collectors.toUnmodifiableSet());

        return new AccessTokenClaims(
                session.userId().asInt(),
                session.username(),
                roles,
                session.sessionVersionAtIssue());
    }
}
