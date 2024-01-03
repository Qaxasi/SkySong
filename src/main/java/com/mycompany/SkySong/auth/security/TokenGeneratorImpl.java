package com.mycompany.SkySong.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenGeneratorImpl implements TokenGenerator {

    @Override
    public String generateToken(Authentication authentication) {
        return null;
    }
}
