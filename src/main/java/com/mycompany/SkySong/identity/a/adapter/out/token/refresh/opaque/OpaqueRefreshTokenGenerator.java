package com.mycompany.SkySong.identity.authentication.adapter.out.token.refresh.opaque;

import com.mycompany.SkySong.identity.authentication.application.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class OpaqueRefreshTokenGenerator implements RefreshTokenGenerator {
    private static final int RAW_LENGTH = 32;
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final SecureRandom random;

    public OpaqueRefreshTokenGenerator(final SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override
    public Result<RefreshToken> generate() {
        final byte[] rnd = new byte[RAW_LENGTH];
        random.nextBytes(rnd);

        final String token = encoder.encodeToString(rnd);
        return RefreshToken.ofGenerated(token);
    }
}