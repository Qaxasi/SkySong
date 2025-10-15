package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public final class AccessVersion {
    private static final int INITIAL_VALUE = 1;
    private final int value;
    private AccessVersion(final int value) {
        this.value = value;
    }

    public static Result<AccessVersion> of(final int value) {
        if (value < INITIAL_VALUE) {
            return Result.failure("Access version must be positive", ErrorType.VALIDATION_ERROR);
        }
        return Result.success(new AccessVersion(value));
    }

    public int value() {
        return value;
    }
    public AccessVersion bump() {
        return new AccessVersion(Math.addExact(value, 1));
    }
    public boolean isSameAs(final AccessVersion current) {
        return this.value == current.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessVersion av)) return false;
        return value == av.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return "AccessVersion{" +
                "value=" + value +
                '}';
    }
}
