package com.mycompany.SkySong.auth.security.unused;

import io.jsonwebtoken.Claims;

public interface ClaimsExtractor {
    Claims getClaimsFromToken(String token);
}
