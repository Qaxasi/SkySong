package com.mycompany.SkySong.identity.registration.application.service;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationData;
import com.mycompany.SkySong.identity.registration.application.port.PasswordHasher;
import com.mycompany.SkySong.identity.registration.domain.User;
import com.mycompany.SkySong.identity.registration.domain.UserRole;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.identity.registration.application.port.UserSaver;
import com.mycompany.SkySong.identity.registration.application.validator.UserRegistrationValidator;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Set;

public class UserRegistration {
    private final UserRegistrationValidator validation;
    private final PasswordHasher passwordHasher;
    private final UserSaver userSaver;

    public UserRegistration(final UserRegistrationValidator validation,
                            final PasswordHasher passwordHasher,
                            final UserSaver userSaver) {
        this.validation = validation;
        this.passwordHasher = passwordHasher;
        this.userSaver = userSaver;
    }

    public Result<Void> register(final UserRegistrationData data) {
        return validatePresent(data)
                .flatMap(ignored -> validation.validateFormatAndUniqueness(data))
                .flatMap(ignored2 -> createUser(data))
                .flatMap(userSaver::saveUser);
    }

    private Result<User> createUser(final UserRegistrationData data) {
        final String hashedPassword = passwordHasher.hash(data.password());
        return new User.Builder()
                .withUsername(data.username())
                .withEmail(data.email())
                .withPassword(hashedPassword)
                .withRoles(Set.of(UserRole.ROLE_USER))
                .build();
    }

    private Result<Void> validatePresent(final UserRegistrationData data) {
        if (data.username() == null || data.username().isBlank()
                || data.email() == null || data.email().isBlank()
                || data.password() == null || data.password().isBlank()) {
            return Result.failure("Missing registration data", ErrorType.VALIDATION_ERROR);
        }
        return Result.success();
    }
}