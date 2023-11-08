package com.mycompany.SkySong.testsupport.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilterUtils {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_MS = 1000L;

    public static String generateTokenWithoutSubject() {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_MS);
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }
    public static String generateTokenWithUnsupportedSignature() {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .setHeaderParam("alg", "none")
                .compact();
    }
    public static String generateTokenWithEmptyClaims() {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }
    public static String generateExpiredToken() {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() - EXPIRATION_MS);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }
}
