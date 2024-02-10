package com.mycompany.SkySong.auth.security;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TokenGeneratorImpl implements TokenGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    @Override
    public String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
