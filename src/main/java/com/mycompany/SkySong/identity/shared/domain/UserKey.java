package com.mycompany.SkySong.identity.shared.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Arrays;
import java.util.Base64;

public final class UserKey {
    public static final int LENGTH = 16;
    private final byte[] bytes;

    private UserKey(byte[] b) {
        this.bytes = b;
    }
    public static Result<UserKey> of(byte[] b) {
        if (b == null || b.length != LENGTH) {
            return Result.failure("invalid user key length", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new UserKey(b.clone()));
    }
    public byte[] bytes() {
        return bytes.clone();
    }
    public String toBase64Url() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static boolean isValid(byte[] b) {
        return b != null && b.length == LENGTH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserKey userKey = (UserKey) o;
        return Arrays.equals(bytes, userKey.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
