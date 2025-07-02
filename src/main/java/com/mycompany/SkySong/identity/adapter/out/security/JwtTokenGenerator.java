package com.mycompany.SkySong.identity.adapter.out.security;

import com.mycompany.SkySong.identity.application.authentication.shared.dto.AccessTokenPayload;
import com.mycompany.SkySong.identity.application.authentication.shared.ports.AccessTokenGenerator;
import com.mycompany.SkySong.identity.domain.AccessToken;
import com.mycompany.SkySong.shared.config.security.jwt.JwtAccessTokenProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenGenerator implements AccessTokenGenerator {
    private final SecretKey signKey;
    private final long accessTokenExpiration;

    public JwtTokenGenerator(final String secretKey,
                             final JwtAccessTokenProperties accessTokenProperties) {
        this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenProperties.getExpiration();
    }

    @Override
    public AccessToken generate(final AccessTokenPayload payload) {
        return generateAccessToken(payload);
    }

    private AccessToken generateAccessToken(final AccessTokenPayload payload) {
        final Map<String, Object> claims = Map.of(
                "userId", payload.userId(),
                "roles", payload.roles()
        );

        return new AccessToken(buildToken(claims, payload.username(), accessTokenExpiration));
    }

    private String buildToken(final Map<String, Object> extraClaims,
                              final String subject,
                              final long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
