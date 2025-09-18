package com.mycompany.SkySong.identity.shared.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.security.MessageDigest;
import java.util.Arrays;

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
    public static boolean isValid(byte[] b) {
        return b != null && b.length == LENGTH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserKey userKey = (UserKey) o;
        return MessageDigest.isEqual(this.bytes, userKey.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
