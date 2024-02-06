package com.mycompany.SkySong.auth.security.unused;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenValidatorImpl implements TokenValidator {
    private final KeyManager key;
    public TokenValidatorImpl(KeyManager key) {
        this.key = key;
    }
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
            log.error(" Invalid JWT token - " + e.getMessage());
            return false;
        }
    }
}
