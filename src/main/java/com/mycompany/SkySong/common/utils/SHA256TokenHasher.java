package com.mycompany.SkySong.common.utils;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
@Service
public class TokenHasher {

    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return encoder.encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
