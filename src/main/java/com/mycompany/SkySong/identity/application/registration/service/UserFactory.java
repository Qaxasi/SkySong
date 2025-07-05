package com.mycompany.SkySong.identity.application.registration.service;

import com.mycompany.SkySong.identity.adapter.out.db.exception.RoleProviderPersistenceException;
import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.application.registration.ports.RoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.identity.application.registration.ports.PasswordHasher;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

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

    public Result<User> createUser(final UserRegistrationInput input) {
        final String hashedPassword = passwordHasher.hash(input.password());
        final Role defaultRole;

        try {
            defaultRole = roleProvider.provideRole(UserRole.ROLE_USER).orElse(null);
        } catch (RoleProviderPersistenceException e) {
            logger.error("Failed to load role from provider", e, context("role", UserRole.ROLE_USER));
            return Result.failure("Unexpected error while loading role", ErrorType.PERSISTENCE_ERROR);
        }

        if (defaultRole == null) {
            logger.warn("Default role not found in system configuration", context("role", UserRole.ROLE_USER.name()));
            return Result.failure("System configuration missing default user role", ErrorType.ROLE_CONFIGURATION_ERROR);
        }

        return Result.success(new User(input.username(), input.email(), hashedPassword, Set.of(defaultRole)));
    }
}
