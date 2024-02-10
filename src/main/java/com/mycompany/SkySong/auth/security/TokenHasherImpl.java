package com.mycompany.SkySong.auth.security;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class TokenHasherImpl implements TokenHasher {

    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    @Override
    public String generateHashedToken(String token) {
        return null;
    }
}
