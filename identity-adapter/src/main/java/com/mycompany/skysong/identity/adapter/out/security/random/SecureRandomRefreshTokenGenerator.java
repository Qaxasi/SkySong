package com.mycompany.skysong.identity.adapter.out.security.random;

import com.mycompany.skysong.identity.application.authentication.port.RefreshTokenGenerator;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.core.result.Result;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureRandomRefreshTokenGenerator implements RefreshTokenGenerator {
    private static final int RAW_LENGTH = 32;
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final SecureRandom random;

    public SecureRandomRefreshTokenGenerator(final SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override
    public Result<RefreshToken> generate() {
        final byte[] rnd = new byte[RAW_LENGTH];
        random.nextBytes(rnd);

        final String token = encoder.encodeToString(rnd);
        return RefreshToken.fromGenerated(token);
    }
}