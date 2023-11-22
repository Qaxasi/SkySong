package com.mycompany.SkySong.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public interface JwtTokenProvider {
    String generateToken(Authentication authentication);
    Claims getClaimsFromToken(String token);
    boolean validateToken(String token);
}
