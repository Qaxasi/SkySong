package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.util.Locale;

public class Email {
    private final String value;

    public Email(final String value) {
        this.value = value;
    }

    public static Result<Email> fromInput(final String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.failure("Invalid email format", ErrorType.VALIDATION_ERROR);
        }

        final String normalized = raw.trim().toLowerCase(Locale.ROOT);

        return Result.success(new Email(normalized));
    }

    public String value() {
        return value;
    }
}
