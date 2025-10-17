package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public final class UserId {
    public static final int LOWEST_VALID = 1;
    private final int value;

    private UserId(final int value) {
        this.value = value;
    }

    public static Result<UserId> of(final int rawUserId) {
        return (rawUserId < LOWEST_VALID) ?
                Result.failure("UserId must be positive", ErrorType.VALIDATION_ERROR)
                : Result.success(new UserId(rawUserId));
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
