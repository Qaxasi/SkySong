package com.mycompany.SkySong.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenGeneratorImpl implements TokenGenerator {
    private final long jwtExpirationDate;
    private final DateProvider dateProvider;
    private final KeyManager key;

    public TokenGeneratorImpl(@Value("${app-jwt-expiration-milliseconds}") long jwtExpirationDate,
                              DateProvider dateProvider,
                              KeyManager key) {
        this.jwtExpirationDate = jwtExpirationDate;
        this.dateProvider = dateProvider;
        this.key = key;
    }
    @Override
    public String generateToken(Authentication authentication) {
        return null;
    }
}
