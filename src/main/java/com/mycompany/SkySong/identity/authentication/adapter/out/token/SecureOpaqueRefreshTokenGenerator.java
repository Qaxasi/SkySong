package com.mycompany.SkySong.identity.authentication.adapter.out.token;

import com.mycompany.SkySong.identity.authentication.application.shared.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.infrastructure.security.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Component
public class SecureOpaqueRefreshTokenGenerator implements RefreshTokenGenerator {
    private final SecureRandom random;
    private final Base64.Encoder encoder;
    private final Clock clock;
    private final Duration refreshTokenTtl;

    public SecureOpaqueRefreshTokenGenerator(final SecureRandom random,
                                             final Base64.Encoder encoder,
                                             final Clock clock,
                                             final RefreshTokenProperties properties) {
        this.random = random;
        this.encoder = encoder;
        this.clock = clock;
        this.refreshTokenTtl = properties.duration();
    }

    @Override
    public Result<RefreshToken> generate() {
        final byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        final String tokenValue = encoder.encodeToString(bytes);
        final Instant expiresAt = Instant.now(clock).plus(refreshTokenTtl);

        return RefreshToken.of(tokenValue, expiresAt);
    }
}
