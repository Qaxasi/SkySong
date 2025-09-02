package com.mycompany.SkySong.identity.authentication.adapter.out.token.refresh.opaque;

import com.mycompany.SkySong.identity.authentication.application.shared.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.UserKeyStore;
import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.infrastructure.security.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Component
public class SecureOpaqueRefreshTokenGenerator implements RefreshTokenGenerator {
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final UserKeyStore userKeyStore;
    private final SecureRandom random;
    private final Clock clock;
    private final Duration refreshTokenTtl;

    public SecureOpaqueRefreshTokenGenerator(final UserKeyStore userKeyStore,
                                             final Clock clock,
                                             final SecureRandom secureRandom,
                                             final RefreshTokenProperties properties) {
        this.userKeyStore = userKeyStore;
        this.random = secureRandom;
        this.clock = clock;
        this.refreshTokenTtl = properties.duration();
    }

    @Override
    public Result<RefreshToken> generate(final int userId) {
        final int userKeyLength = 16;
        final int randomTokenLength = 32;

        final Optional<byte[]> userKey = userKeyStore.getForUser(userId);

        final byte[] randomToken = new byte[randomTokenLength];
        random.nextBytes(randomToken;

        final ByteBuffer byteBuffer = ByteBuffer.allocate(userKeyLength + randomTokenLength);
        byteBuffer.put(userKey.get()).put(randomToken);

        final String value = encoder.encodeToString(byteBuffer.array());
        final Instant expiresAt = Instant.now(clock).plus(refreshTokenTtl);

        return RefreshToken.of(value, expiresAt);
    }
}
