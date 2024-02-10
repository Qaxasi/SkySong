package com.mycompany.SkySong.auth.security;

import java.util.Base64;

public class TokenHasherImpl implements TokenHasher {

    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    @Override
    public String generateHashedToken(String token) {
        return null;
    }
}
