package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;

public interface ClaimsExtractor {
    Claims getClaimsFromToken(String token);
}
