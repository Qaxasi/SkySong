package com.mycompany.SkySong.testutils.auth;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class TestJwtTokenGenerator {
    private final String secretKey;
    private final long jwtExpiration;
    private final long refreshJwtExpiration;

    public TestJwtTokenGenerator() {
        this.secretKey = "wJ4ds7VbRmFHRP4fX5QbJmTcYZv5P1ZkVN7/kO4id8E=";
        this.jwtExpiration = 600000;
        this.refreshJwtExpiration = 86400000;
    }

    public TestJwtTokenGenerator(String secretKey, long jwtExpiration, long refreshJwtExpiration) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.refreshJwtExpiration = refreshJwtExpiration;
    }
    public String generateValidRefreshToken(String username) {
        JwtTokenManager jwtTokenManager = new JwtTokenManager(secretKey, jwtExpiration, refreshJwtExpiration);

        CustomUserDetails userDetails = new CustomUserDetails(
                1,
                username,
                "alex@mail.mail",
                "Password#3",
                new HashSet<>()
        );
        return jwtTokenManager.generateRefreshToken(userDetails);
    }

    public String generateValidAccessToken() {
        return Jwts.builder()
                .setSubject("Alex")
                .claim("roles", List.of("ROLE_USER"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(generateSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String createExpiredToken() {
        return Jwts.builder()
                .setSubject("Alex")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(generateSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createUnsupportedToken() {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

        return Jwts.builder()
                .setSubject("Alex")
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Key generateSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}