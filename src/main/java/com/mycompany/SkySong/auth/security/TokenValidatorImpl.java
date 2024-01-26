package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class TokenValidatorImpl implements TokenValidator {
    private final KeyManager key;
    public TokenValidatorImpl(KeyManager key) {
        this.key = key;
    }
    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject() != null && !claims.getSubject().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
