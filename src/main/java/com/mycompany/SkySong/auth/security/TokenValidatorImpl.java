package com.mycompany.SkySong.auth.security;

import org.springframework.stereotype.Component;

@Component
public class TokenValidatorImpl implements TokenValidator {
    private final KeyManager keyManager;
    private final JwtExceptionHandler jwtExceptionHandler;

    public TokenValidatorImpl(KeyManager keyManager, JwtExceptionHandler jwtExceptionHandler) {
        this.keyManager = keyManager;
        this.jwtExceptionHandler = jwtExceptionHandler;
    }
    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
