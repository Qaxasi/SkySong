package com.mycompany.SkySong.identity.shared.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum UserRole {
    USER("USER", "ROLE_USER"),
    ADMIN("ADMIN", "ROLE_ADMIN");

    private static final Map<String, UserRole> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(UserRole::code, r -> r));

    private final String code;
    private final String authority;

    UserRole(String code, String authority) {
        this.code = code;
        this.authority = authority;
    }
    public static Result<UserRole> fromCode(String code) {
        if (code == null || code.isBlank() || code.trim().isBlank()) {
            return Result.failure("Role code must not be null or blank", ErrorType.INVARIANT_VIOLATION);
        }
        final UserRole role = BY_CODE.get(code.trim().toUpperCase(Locale.ROOT));
        return Result.success(role);
    }

    public String code() {
        return code;
    }

    public String authority() {
        return authority;
    }
}
