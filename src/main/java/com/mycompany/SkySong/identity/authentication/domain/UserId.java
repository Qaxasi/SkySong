package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Objects;

public final class UserId {
    private final int value;

    private UserId(final int value) {
        this.value = value;
    }

    public static Result<UserId> of(final int value) {
        return (value <= 0) ?
                Result.failure("UserId must be positive", ErrorType.VALIDATION_ERROR)
                : Result.success(new UserId(value));
    }

    public int asInt() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserId) obj;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserId[" +
                "value=" + value + ']';
    }
}
