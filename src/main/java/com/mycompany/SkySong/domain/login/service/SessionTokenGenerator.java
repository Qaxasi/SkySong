package com.mycompany.SkySong.login.domain.service;

import java.security.SecureRandom;
import java.util.Base64;

class SessionTokenGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
