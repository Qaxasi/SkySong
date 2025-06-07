package com.mycompany.SkySong.identity.application.registration.service;

import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.application.registration.exception.UserRoleConfigurationException;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.identity.application.registration.ports.PasswordHasher;
import com.mycompany.SkySong.identity.application.registration.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.shared.error.ErrorType;

import java.util.Set;

public class UserFactory {
    private final PasswordHasher passwordHasher;
    private final RegistrationRoleRepository roleRepository;

    public UserFactory(final PasswordHasher passwordEncoder,
                       final RegistrationRoleRepository roleRepository) {
        this.passwordHasher = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(final UserRegistrationInput input) {
        final String hashedPassword = passwordHasher.hash(input.password());
        final Role defaultRole = fetchDefaultUserRole();

        return new User(input.username(), input.email(), hashedPassword, Set.of(defaultRole));
    }

    private Role fetchDefaultUserRole() {
        return roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new UserRoleConfigurationException(
                        "Default user role not found in the system.",
                        ErrorType.IDENTITY_CONFIGURATION_ERROR));
    }
}
