package com.mycompany.SkySong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.util.Map;
import java.util.Objects;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private static final Map<String, UserRole> BY_CODE =
            Map.of("USER", USER, "ADMIN", ADMIN);

    private final String code;

    UserRole(final String code) {
        this.code = Objects.requireNonNull(code, "Role code must not be null");
    }

    public static Result<UserRole> fromStored(final String storedCode) {
        if (storedCode == null || storedCode.isBlank()) {
            return Result.failure("Role code must not be null or blank", ErrorType.DATA_INTEGRITY_ERROR);
        }

        final UserRole role = BY_CODE.get(storedCode);
        if (role == null) {
            return Result.failure("Unknown role code: " + storedCode, ErrorType.DATA_INTEGRITY_ERROR);
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
