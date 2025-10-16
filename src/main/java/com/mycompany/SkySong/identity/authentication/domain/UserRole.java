package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Locale;

public enum UserRole {
    USER, ADMIN;

    public static Result<UserRole> fromCode(final String rawCode) {
        if (rawCode == null || rawCode.isBlank()) {
            return Result.failure("Role code must not be null or blank", ErrorType.VALIDATION_ERROR);
        }

        final String normalizedCode = rawCode.trim().toUpperCase(Locale.ROOT);

        try {
            return Result.success(UserRole.valueOf(normalizedCode));
        } catch (IllegalArgumentException ex) {
            return Result.failure("Unknown role code", ErrorType.VALIDATION_ERROR);
        }
    }

    public String code() {
        return name();
    }

    public String toAuthority() {
        return "ROLE_" + name();
    }
}
