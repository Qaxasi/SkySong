package com.mycompany.SkySong.identity.authentication.adapter.out.token.access.jwt;

import com.mycompany.SkySong.identity.authentication.application.shared.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.shared.domain.UserRole;
import com.mycompany.SkySong.infrastructure.security.jwt.JwtAccessTokenProperties;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.time.Clock;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenGenerator implements AccessTokenGenerator {
    private final SecretKey signKey;
    private final Duration accessTokenExpiration;
    private final Clock clock;

    public JwtTokenGenerator(final SecretKey signKey,
                             final JwtAccessTokenProperties accessTokenProperties,
                             final Clock clock) {
        this.signKey = signKey;
        this.accessTokenExpiration = accessTokenProperties.expiration();
        this.clock = clock;
    }

    @Override
    public AccessToken generate(final AccessTokenClaims claims) {
        final Instant now = clock.instant();
        final Instant exp = now.plus(accessTokenExpiration);

        final String subject = String.valueOf(claims.userId().asInt());

        final Set<String> roles = claims.roles().stream()
                .map(UserRole::code)
                .collect(Collectors.toUnmodifiableSet());

        final Map<String, Object> extraClaims = Map.of(
                "roles", roles,
                "authz_version", claims.authzVersion()
        );

        final String jwt = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .addClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        return new AccessToken(jwt);
    }
}
