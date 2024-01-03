package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class ClaimsExtractorImpl implements ClaimsExtractor {
    private final KeyManager key;

    public ClaimsExtractorImpl(KeyManager key) {
        this.key = key;
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return null;
    }
}
