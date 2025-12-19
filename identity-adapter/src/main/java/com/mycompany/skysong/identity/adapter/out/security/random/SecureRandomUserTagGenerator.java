package com.mycompany.skysong.identity.adapter.out.security.random;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.application.user.registration.port.UserTagGenerator;
import com.mycompany.skysong.identity.domain.UserTag;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
class SecureRandomUserTagGenerator implements UserTagGenerator {
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
