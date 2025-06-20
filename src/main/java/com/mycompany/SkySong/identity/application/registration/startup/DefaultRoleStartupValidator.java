package com.mycompany.SkySong.identity.application.registration.startup;

import com.mycompany.SkySong.identity.application.registration.exception.ApplicationStartupException;
import com.mycompany.SkySong.identity.application.registration.ports.RoleProvider;
import com.mycompany.SkySong.identity.domain.UserRole;

public class DefaultRoleStartupValidator {
    private final RoleProvider roleProvider;

    public DefaultRoleStartupValidator(final RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    public void validate() {
        roleProvider.provideRole(UserRole.ROLE_USER).orElseThrow(
                () -> new ApplicationStartupException("Missing default role: ROLE_USER"));
    }
}
