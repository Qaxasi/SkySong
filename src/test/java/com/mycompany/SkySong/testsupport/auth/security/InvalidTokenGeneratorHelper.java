package com.mycompany.SkySong.testsupport.auth.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
@Component
public class InvalidTokenGeneratorHelper {
    private final long expiration_ms = 1000L;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static Date getCurrentDate() {
        return new Date();
    }
    private Date getExpirationDate(boolean expired) {
        Date now = getCurrentDate();
        return new Date(now.getTime() + (expired ? -expiration_ms : expiration_ms));
    }
    private JwtBuilder createBaseBuilder(Date now, Date expirationDate) {
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key);
    }
    public String generateMalformedToken() {
        String token = createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setSubject("User")
                .compact();
        return token.substring(0, token.length() / 2);
    }
    public String generateTokenWithUnsupportedSignature() {
        Key strongerKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setSubject("User")
                .signWith(strongerKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateTokenWithEmptyClaims() {
        return createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setClaims(new HashMap<>())
                .compact();
    }
    public String generateExpiredToken() {
        return createBaseBuilder(getCurrentDate(), getExpirationDate(true))
                .setSubject("User")
                .compact();
    }
    public String generateTokenWithInvalidSignature() {
        String token = createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setSubject("User")
                .compact();
        return token + "invalid";
    }
}
