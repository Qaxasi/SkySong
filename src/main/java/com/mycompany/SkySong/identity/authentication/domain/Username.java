package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

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
        this.value = Objects.requireNonNull(value, "Username.value must not be null");
    }

    public static Result<Username> of(final String rawUsername) {
        if (rawUsername == null) {
            return Result.failure("Username must not be null", ErrorType.VALIDATION_ERROR);
        }
        final String normalizedUsername = rawUsername.strip().toLowerCase(Locale.ROOT);
        final int length = normalizedUsername.length();

        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            return Result.failure(
                    String.format("Username length must be between %d and %d", MIN_LENGTH, MAX_LENGTH),
                    ErrorType.VALIDATION_ERROR);
        }

        if (!USERNAME_PATTERN.matcher(normalizedUsername).matches()) {
            return Result.failure("Username must start with a letter and contain only a–z and 0–9", ErrorType.VALIDATION_ERROR);
        }
        return Result.success(new Username(normalizedUsername));
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
