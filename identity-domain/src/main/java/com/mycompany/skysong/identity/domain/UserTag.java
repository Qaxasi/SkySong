package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.util.Objects;
import java.util.regex.Pattern;

public final class UserTag {
    public static final int LENGTH = 22;
    public static final String FORMAT_REGEX = "^[A-Za-z0-9_-]{" + LENGTH + "}$";
    private static final Pattern FORMAT =  Pattern.compile(FORMAT_REGEX);

    private final String value;
    private UserTag(final String value) {
        this.value = Objects.requireNonNull(value, "UserTag value must not be null");
    }
    public static Result<UserTag> fromInput(final String userTag) {
        return validate(userTag, ErrorType.VALIDATION_ERROR);
    }
    public static Result<UserTag> ofGenerated(final String userTag) {
        return validate(userTag, ErrorType.INVARIANT_VIOLATION);
    }

    public static Result<UserTag> fromStored(final String userTag) {
        return validate(userTag, ErrorType.DATA_INTEGRITY_ERROR);
    }

    private static Result<UserTag> validate(final String userTag, final ErrorType errorType) {
        if (userTag == null || userTag.isBlank()) {
            return Result.failure("User tag must not be null or blank", errorType);
        }
        if (!FORMAT.matcher(userTag).matches()) {
            return Result.failure("Invalid user tag format", errorType);
        }
        return Result.success(new UserTag(userTag));
    }

    public String value() {
        return value;
    }
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof UserTag other && value.equals(other.value));
    }
    @Override public int hashCode() {
        return value.hashCode();
    }
    @Override public String toString() {
        return value;
    }
}
