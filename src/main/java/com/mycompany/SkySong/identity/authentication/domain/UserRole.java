package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Map;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private static final Map<String, UserRole> BY_CODE =
            Map.of("USER", USER, "ADMIN", ADMIN);

    private final String code;

    UserRole(final String code) {
        this.code = code;
    }

    public static Result<UserRole> fromPersistence(final String persistedCode) {
        if (persistedCode == null || persistedCode.isBlank()) {
            return Result.failure("Role code must not be null or blank", ErrorType.DATA_INTEGRITY_ERROR);
        }

        final UserRole role = BY_CODE.get(persistedCode);
        if (role == null) {
            return Result.failure("Unknown role code: " + persistedCode, ErrorType.DATA_INTEGRITY_ERROR);
        }
        return Result.success(role);
    }

    public String code() {
        return code;
    }

    public String toAuthority() {
        return "ROLE_" + code;
    }
}
