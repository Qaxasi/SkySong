package com.mycompany.SkySong.testsupport.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InvalidTokenGenerator {
    private static final long EXPIRATION_MS = 1000L;
    private static Date getCurrentDate() {
        return new Date();
    }
    private static Date getExpirationDate(boolean expired) {
        Date now = getCurrentDate();
        return new Date(now.getTime() + (expired ? -EXPIRATION_MS : EXPIRATION_MS));
    }
    private static JwtBuilder createBaseBuilder(Date now, Date expirationDate, Key key) {
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key);
    }

    public static String generateTokenWithoutSubject(Key key) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_MS);
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
    public static String generateTokenWithUnsupportedSignature(Key key) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
    public static String generateTokenWithEmptyClaims(Key key) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
    public static String generateExpiredToken(String username, Key key) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() - EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
