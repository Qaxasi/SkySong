package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public final class AccessVersion {
    private final int value;
    private AccessVersion(int value) {
        this.value = value;
    }

    public static Result<AccessVersion> of(final int value) {
        if (value < 0) {
            return Result.failure("Access version must be positive", ErrorType.INVARIANT_VIOLATION);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessVersion that = (AccessVersion) o;
        return value == that.value;
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
