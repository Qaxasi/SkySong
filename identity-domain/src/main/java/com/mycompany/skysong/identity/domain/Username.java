package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Username {
    public static final int MIN_LENGTH = 5;
    public static final int MAX_LENGTH = 25;

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile( "^[a-z][a-z0-9]*$");

    private final String value;
    private Username(final String value) {
        this.value = Objects.requireNonNull(value, "Username value must not be null");
    }

    public static Result<Username> fromInput(final String rawUsername) {
        if (rawUsername == null) {
            return Result.failure("Username must not be null", ErrorType.VALIDATION_ERROR);
        }
        final String normalizedUsername = normalize(rawUsername);
        return validate(normalizedUsername, ErrorType.VALIDATION_ERROR);
    }

    public static Result<Username> fromStored(final String username) {
        if (username == null) {
            return Result.failure("Username must not be null", ErrorType.DATA_INTEGRITY_ERROR);
        }

        if (!isNormalized(username)) {
            return Result.failure("Username must be already normalized", ErrorType.DATA_INTEGRITY_ERROR);
        }

        return validate(username, ErrorType.DATA_INTEGRITY_ERROR);
    }

    private static Result<Username> validate(final String username,
                                             final ErrorType errorType) {
        final int length = username.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            return Result.failure(
                    String.format("Username length must be between %d and %d", MIN_LENGTH, MAX_LENGTH), errorType);
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return Result.failure("Username must start with a letter and contain only a–z and 0–9", errorType);
        }
        return Result.success(new Username(username));
    }

    private static String normalize(final String s) {
        return s.strip().toLowerCase(Locale.ROOT);
    }

    private static boolean isNormalized(final String s) {
        return s.equals(normalize(s));
    }

    public String asString() {
        return value;
    }
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof Username other && value.equals(other.value));
    }
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    @Override
    public String toString() {
        return value;
    }
}
