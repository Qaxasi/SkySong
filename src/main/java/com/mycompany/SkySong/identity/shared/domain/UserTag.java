package com.mycompany.SkySong.identity.shared.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.security.MessageDigest;
import java.util.Arrays;

public final class UserTag {
    public static final int LENGTH = 16;
    private final byte[] bytes;

    private UserTag(byte[] b) {
        this.bytes = b.clone();
    }
    public static Result<UserTag> of(byte[] b) {
        if (b == null || b.length != LENGTH) {
            return Result.failure("invalid user tag length", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new UserTag(b));
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
        final UserTag other = (UserTag) o;
        return MessageDigest.isEqual(this.bytes, other.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
