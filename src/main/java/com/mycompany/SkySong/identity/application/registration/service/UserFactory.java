package com.mycompany.SkySong.identity.application.registration.service;

import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.application.registration.exception.UserRoleConfigurationException;
import com.mycompany.SkySong.identity.application.registration.ports.RoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.identity.application.registration.ports.PasswordHasher;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;

import java.util.Set;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserFactory {
    private final PasswordHasher passwordHasher;
    private final RoleProvider roleProvider;
    private final ApplicationLogger logger;

    public UserFactory(final PasswordHasher passwordEncoder,
                       final RoleProvider roleProvider,
                       final ApplicationLogger logger) {
        this.passwordHasher = passwordEncoder;
        this.roleProvider = roleProvider;
        this.logger = logger;
    }

    public User createUser(final UserRegistrationInput input) {
        final String hashedPassword = passwordHasher.hash(input.password());
        final Role defaultRole = roleProvider.provideRole(UserRole.ROLE_USER).orElseThrow(
                () -> {
                    logger.error("Default role not found in system configuration", context("role", UserRole.ROLE_USER.name()));
                    return new UserRoleConfigurationException("System configuration missing default user role.",
                            ErrorType.ROLE_CONFIGURATION_ERROR);
                });
        return new User(input.username(), input.email(), hashedPassword, Set.of(defaultRole));
    }
}
