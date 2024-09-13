package com.mycompany.SkySong.adapter.security;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import org.junit.jupiter.api.BeforeEach;

class JwtTokenTest {

    private JwtTokenManager jwtManager;

    @BeforeEach
    void setup() {
        String secretKey = "-c4iY6yOly4Io2kfebbk/sGdU6Lq1lSS2DX7XvNr0agM=";
        long jwtExpiration = 100000L;
        long refreshJwtExpiration = 200000L;
        jwtManager = new JwtTokenManager(secretKey, jwtExpiration, refreshJwtExpiration);
    }
}
