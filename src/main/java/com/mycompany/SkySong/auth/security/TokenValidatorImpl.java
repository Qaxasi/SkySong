package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class TokenValidatorImpl implements TokenValidator {
    private final KeyManager key;
    private final JwtExceptionHandler jwtExceptionHandler;

    public TokenValidatorImpl(KeyManager key, JwtExceptionHandler jwtExceptionHandler) {
        this.key = key;
        this.jwtExceptionHandler = jwtExceptionHandler;
    }
    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
