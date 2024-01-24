package com.mycompany.SkySong.testsupport.auth.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

public class InvalidTokenGeneratorHelper {
    private static final long EXPIRATION_MS = 1000L;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static Date getCurrentDate() {
        return new Date();
    }
    private static Date getExpirationDate(boolean expired) {
        Date now = getCurrentDate();
        return new Date(now.getTime() + (expired ? -EXPIRATION_MS : EXPIRATION_MS));
    }
    private static JwtBuilder createBaseBuilder(Date now, Date expirationDate) {
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key);
    }
    public static String generateMalformedToken() {
        String token = createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setSubject("User")
                .compact();
        return token.substring(0, token.length() / 2);
    }
    public static String generateTokenWithUnsupportedSignature() {
        Key strongerKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setSubject("User")
                .signWith(strongerKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public static String generateTokenWithEmptyClaims() {
        return createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setClaims(new HashMap<>())
                .compact();
    }
    public static String generateExpiredToken() {
        return createBaseBuilder(getCurrentDate(), getExpirationDate(true))
                .setSubject("User")
                .compact();
    }
    public static String generateTokenWithInvalidSignature() {
        String token = createBaseBuilder(getCurrentDate(), getExpirationDate(false))
                .setSubject("User")
                .compact();
        return token + "invalid";
    }
}
