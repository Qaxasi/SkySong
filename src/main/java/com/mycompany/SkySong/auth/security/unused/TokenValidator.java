package com.mycompany.SkySong.auth.security;

public interface TokenValidator {
    boolean validateToken(String token);
}
