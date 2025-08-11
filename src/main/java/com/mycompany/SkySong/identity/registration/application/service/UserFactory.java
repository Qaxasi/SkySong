package com.mycompany.SkySong.identity.registration.application.service;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.registration.application.port.RoleProvider;
import com.mycompany.SkySong.identity.registration.domain.User;
import com.mycompany.SkySong.identity.registration.application.port.PasswordHasher;
import com.mycompany.SkySong.identity.registration.domain.UserRole;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Set;

public class UserFactory {
    private final PasswordHasher passwordHasher;
    private final RoleProvider roleProvider;
    public UserFactory(final PasswordHasher passwordEncoder,
                       final RoleProvider roleProvider) {
        this.passwordHasher = passwordEncoder;
        this.roleProvider = roleProvider;
    }

    public Result<User> createUser(final UserRegistrationInput input) {
        final String hashedPassword = passwordHasher.hash(input.password());

        return roleProvider.provideRole(UserRole.ROLE_USER)
                .flatMap(defaultRole -> new User.Builder()
                        .withUsername(input.username())
                        .withEmail(input.email())
                        .withPassword(hashedPassword)
                        .withRoles(Set.of(defaultRole))
                        .build());
    }
}
