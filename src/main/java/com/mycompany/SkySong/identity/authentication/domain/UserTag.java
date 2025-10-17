package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Objects;
import java.util.regex.Pattern;

public final class UserTag {
    public static final int LENGTH = 22;
    public static final String FORMAT_REGEX = "^[A-Za-z0-9_-]{" + LENGTH + "}$";
    private static final Pattern FORMAT =  Pattern.compile(FORMAT_REGEX);

    private final String value;
    private UserTag(final String value) {
        this.value = value;
    }

    public static Result<UserTag> of(final String rawUserTag) {
        if (rawUserTag == null) {
            return Result.failure("User tag must not be null", ErrorType.VALIDATION_ERROR);
        }
        if (!FORMAT.matcher(rawUserTag).matches()) {
            return Result.failure("Invalid user tag format", ErrorType.VALIDATION_ERROR);
        }
        return Result.success(new UserTag(rawUserTag));
    }

    public String asString() {
        return value;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTag other)) return false;
        return Objects.equals(this.value, other.value);
    }
    @Override public int hashCode() { return value.hashCode(); }
    @Override public String toString() { return value; }
}
