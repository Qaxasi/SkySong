package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class ClaimsExtractorImpl implements ClaimsExtractor {
    @Override
    public Claims getClaimsFromToken(String token) {
        return null;
    }
}
