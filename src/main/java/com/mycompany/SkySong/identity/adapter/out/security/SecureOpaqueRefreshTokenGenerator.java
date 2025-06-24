package com.mycompany.SkySong.identity.adapter.authentication.login.out;

import com.mycompany.SkySong.identity.application.authentication.dto.RefreshToken;
import com.mycompany.SkySong.identity.application.authentication.ports.RefreshTokenGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureOpaqueRefreshTokenGenerator implements RefreshTokenGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    @Override
    public RefreshToken generate() {
        final byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return new RefreshToken(ENCODER.encodeToString(bytes));
    }
}
