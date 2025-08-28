package com.mycompany.SkySong.identity.authentication.adapter.out.token.refresh.opaque;

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
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final SecureRandom random;
    private final Clock clock;
    private final Duration refreshTokenTtl;

    public SecureOpaqueRefreshTokenGenerator(final Clock clock,
                                             final RefreshTokenProperties properties) {
        this.random = new SecureRandom();
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
