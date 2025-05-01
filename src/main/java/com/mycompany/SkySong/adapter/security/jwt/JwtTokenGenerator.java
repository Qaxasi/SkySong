package com.mycompany.SkySong.adapter.security.jwt;

import com.mycompany.SkySong.application.user.login.model.AuthenticatedUser;
import com.mycompany.SkySong.application.user.login.model.AuthenticationTokens;
import com.mycompany.SkySong.application.user.login.port.LoginTokenGenerator;
import com.mycompany.SkySong.application.user.session.port.AccessTokenGenerator;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerator implements LoginTokenGenerator, AccessTokenGenerator {
    private final SecretKey signKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenGenerator(@Value("${application.security.jwt.secret-key}") String secretKey,
                             @Value("${application.security.jwt.expiration}") long accessTokenExpiration,
                             @Value("${application.security.jwt.refresh-token.expiration}") long refreshTokenExpiration) {
        this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public AuthenticationTokens generate(final AuthenticatedUser user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = buildToken(new HashMap<>(), user.usernameOrEmail(), refreshTokenExpiration);
        return new AuthenticationTokens(accessToken, refreshToken);
    }

    @Override
    public String generateAccessToken(final AuthenticatedUser user) {
        Map<String, Object> claims = Map.of(
                "userId", user.id(),
                "roles", user.roles()
        );
        return buildToken(claims, user.usernameOrEmail(), accessTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              String subject,
                              long expiration) {
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
