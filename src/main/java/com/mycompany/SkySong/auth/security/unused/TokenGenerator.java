package com.mycompany.SkySong.auth.security;

import org.springframework.security.core.Authentication;

public interface TokenGenerator {
    String generateToken(Authentication authentication);
}
