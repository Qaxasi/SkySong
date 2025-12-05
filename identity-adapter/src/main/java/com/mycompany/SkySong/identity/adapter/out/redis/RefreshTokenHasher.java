package com.mycompany.SkySong.identity.adapter.out.redis;

import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
final class RefreshTokenHasher {
    private static final Base64.Decoder B64_DEC = Base64.getUrlDecoder();
    private static final Base64.Encoder B64_ENC = Base64.getUrlEncoder().withoutPadding();

    Result<String> hash(final RefreshToken refreshToken) {
        final byte[] raw;
        try {
            raw = B64_DEC.decode(refreshToken.value());
        } catch (IllegalArgumentException ex) {
            return Result.failure("Invalid refresh token", ErrorType.INVALID_REFRESH_TOKEN);
        }

        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] digest = md.digest(raw);
            return Result.success(B64_ENC.encodeToString(digest));
        } catch (NoSuchAlgorithmException ex) {
            return Result.failure("Internal hashing error", ErrorType.INTERNAL_ERROR);
        }
    }
}
