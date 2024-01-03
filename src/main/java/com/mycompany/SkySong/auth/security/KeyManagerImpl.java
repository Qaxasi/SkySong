package com.mycompany.SkySong.auth.security;

import org.springframework.beans.factory.annotation.Value;

import java.security.Key;

public class KeyManagerImpl implements KeyManager {
    private final String jwtSecret;

    public KeyManagerImpl(@Value("${JWT_SECRET}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
    @Override
    public Key getKey() {
        return null;
    }
}
