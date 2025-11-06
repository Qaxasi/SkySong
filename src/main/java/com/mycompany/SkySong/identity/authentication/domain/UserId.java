package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public final class UserId {
    public static final int LOWEST_VALID = 1;
    private final int value;

    private UserId(final int value) {
        this.value = value;
    }

    public static Result<UserId> restore(final int userId) {
        if (userId < LOWEST_VALID) {
            return Result.failure("User id must be positive", ErrorType.DATA_INTEGRITY_ERROR);
        }
        return Result.success(new UserId(userId));
    }

    public int asInt() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof UserId other && value == other.value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
