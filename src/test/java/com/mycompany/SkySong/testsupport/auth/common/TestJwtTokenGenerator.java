package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;

import java.util.HashSet;

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
}