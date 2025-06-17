package com.mycompany.SkySong.identity.application.registration.service;

import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.application.registration.ports.DefaultRoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.identity.application.registration.ports.PasswordHasher;

import java.util.Set;

public class UserFactory {
    private final PasswordHasher passwordHasher;
    private final DefaultRoleProvider roleProvider;

    public UserFactory(final PasswordHasher passwordEncoder,
                       final DefaultRoleProvider roleProvider) {
        this.passwordHasher = passwordEncoder;
        this.roleProvider = roleProvider;
    }

    public User createUser(final UserRegistrationInput input) {
        final String hashedPassword = passwordHasher.hash(input.password());
        final Role defaultRole = roleProvider.provideDefaultRole();

        return new User(input.username(), input.email(), hashedPassword, Set.of(defaultRole));
    }
}
