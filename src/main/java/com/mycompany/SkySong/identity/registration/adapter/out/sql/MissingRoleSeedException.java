package com.mycompany.SkySong.identity.registration.adapter.out.sql;

import com.mycompany.SkySong.identity.registration.domain.UserRole;

public class MissingRoleSeedException extends RuntimeException {
    private final UserRole role;
    public MissingRoleSeedException(UserRole role) {
        super("Missing default role: " + role.name());
        this.role = role;
    }

    public UserRole role() {
        return role;
    }
}
