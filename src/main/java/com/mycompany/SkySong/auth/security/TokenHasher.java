package com.mycompany.SkySong.auth.security;

public interface TokenHasher {
    String generateHashedToken(String token);
}
