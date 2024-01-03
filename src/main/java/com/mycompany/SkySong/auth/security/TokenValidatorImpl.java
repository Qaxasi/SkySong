package com.mycompany.SkySong.auth.security;

import org.springframework.stereotype.Component;

@Component
public class TokenValidatorImpl implements TokenValidator {
    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
