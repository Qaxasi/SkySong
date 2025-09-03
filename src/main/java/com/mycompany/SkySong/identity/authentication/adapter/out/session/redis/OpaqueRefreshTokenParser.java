package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.adapter.out.shared.token.OpaqueRefreshTokenFormat;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Arrays;
import java.util.Base64;

class OpaqueRefreshTokenParser {
    Result<String> extractUserKey(final String token) {
        try {
            final byte[] raw = Base64.getUrlDecoder().decode(token);
            if (raw.length != OpaqueRefreshTokenFormat.RAW_TOKEN_LENGTH) {
                return Result.failure("Invalid token", ErrorType.INVALID_REFRESH_TOKEN);
            }
            final byte[] userKeyBytes = Arrays.copyOfRange(raw, 0, OpaqueRefreshTokenFormat.USER_KEY_LENGTH);
            final String userKeyTag = Base64.getUrlEncoder().withoutPadding().encodeToString(userKeyBytes);
            return Result.success(userKeyTag);
        } catch (IllegalArgumentException e) {
            return Result.failure("Invalid token", ErrorType.INVALID_REFRESH_TOKEN);
        }
    }
}
