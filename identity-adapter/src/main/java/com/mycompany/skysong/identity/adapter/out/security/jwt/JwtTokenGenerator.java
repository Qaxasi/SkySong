package com.mycompany.skysong.identity.adapter.out.security.jwt;

import com.mycompany.skysong.identity.application.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.authentication.port.AccessTokenGenerator;
import com.mycompany.skysong.identity.config.AccessTokenProperties;
import com.mycompany.skysong.identity.domain.UserRole;
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
    private final Duration ttl;
    private final Clock clock;

    public JwtTokenGenerator(final SecretKey signKey,
                             final AccessTokenProperties accessTokenProperties,
                             final Clock clock) {
        this.signKey = signKey;
        this.ttl = accessTokenProperties.ttl();
        if (ttl.isZero() || ttl.isNegative()) {
            throw new IllegalStateException("access token TTL must be positive");
        }
        this.clock = clock;
    }

    @Override
    public AccessToken generate(final AccessTokenClaims claims) {
        final Instant now = clock.instant();
        final Instant exp = now.plus(ttl);

        final String subject = String.valueOf(claims.userId().asInt());

        final Set<String> roles = claims.roles().stream()
                .map(UserRole::code)
                .collect(Collectors.toUnmodifiableSet());

        final Map<String, Object> extraClaims = Map.of(
                "roles", roles
        );

        final String jwt = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .addClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        return new AccessToken(jwt, exp);
    }
}
