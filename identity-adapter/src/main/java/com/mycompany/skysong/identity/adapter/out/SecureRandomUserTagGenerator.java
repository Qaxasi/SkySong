package com.mycompany.SkySong.identity.registration.adapter.out;

import com.mycompany.SkySong.identity.registration.application.port.UserTagGenerator;
import com.mycompany.SkySong.identity.a.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureRandomUserTagGenerator implements UserTagGenerator {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final int RAW_BYTES = 16;
    private final SecureRandom secureRandom;

    public SecureRandomUserTagGenerator(final SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    public Result<UserTag> generate() {
        final byte[] bytes = new byte[RAW_BYTES];
        secureRandom.nextBytes(bytes);
        final String text = ENCODER.encodeToString(bytes);
        return UserTag.ofGenerated(text);
    }
}
