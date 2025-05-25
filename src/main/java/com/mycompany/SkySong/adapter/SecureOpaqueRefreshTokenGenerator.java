package com.mycompany.SkySong.adapter;

import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;
import com.mycompany.SkySong.application.user.authentication.ports.RefreshTokenGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureOpaqueRefreshTokenGenerator implements RefreshTokenGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    @Override
    public RefreshToken generate() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return new RefreshToken(ENCODER.encodeToString(bytes));
    }
}
