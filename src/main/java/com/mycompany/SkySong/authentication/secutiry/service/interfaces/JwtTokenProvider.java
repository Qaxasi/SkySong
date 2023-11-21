package com.mycompany.SkySong.authentication.secutiry.service.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public interface JwtTokenProvider {
    String generateToken(Authentication authentication);
    Claims getClaimsFromToken(String token);
    boolean validateToken(String token);
}
