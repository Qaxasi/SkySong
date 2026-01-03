package com.mycompany.skysong.identity.application.user.registration.service;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.user.registration.port.PasswordHasher;
import com.mycompany.skysong.identity.application.user.registration.port.UserSaver;
import com.mycompany.skysong.identity.domain.port.UserTagGenerator;
import com.mycompany.skysong.identity.application.user.registration.port.UserUniquenessChecker;
import com.mycompany.skysong.identity.domain.*;

public class RegisterUser {
    private final UserUniquenessChecker uniquenessChecker;
    private final PasswordHasher passwordHasher;
    private final UserTagGenerator tagGenerator;
    private final UserSaver userSaver;

    public RegisterUser(final UserUniquenessChecker uniquenessChecker,
                        final PasswordHasher passwordHasher,
                        final UserTagGenerator tagGenerator,
                        final UserSaver userSaver) {
        this.passwordHasher = passwordHasher;
        this.tagGenerator = tagGenerator;
        this.userSaver = userSaver;
        this.uniquenessChecker = uniquenessChecker;
    }

    public Result<Unit> register(final Username username,
                                 final Email email,
                                 final RawPassword rawPassword) {
        try (rawPassword) {
            return PasswordPolicy.validateForRegistration(rawPassword)
                    .flatMap(ignored -> validateUniqueness(username, email))
                    .flatMap(ignored2 -> createUser(username, email, rawPassword))
                    .flatMap(userSaver::save);
        }
    }

    private Result<Unit> validateUniqueness(final Username username,
                                            final Email email) {
        return uniquenessChecker.check(username, email)
                .flatMap(status -> {
                    if (status.usernameExists()) {
                        return Result.failure(
                                "Username already exists",
                                ErrorType.VALIDATION_ERROR);
                    }
                    if (status.emailExists()) {
                        return Result.failure(
                                "Email already exists",
                                ErrorType.VALIDATION_ERROR);
                    }
                    return Result.success();
                });
    }

    private Result<User> createUser(final Username username,
                                    final Email email,
                                    final RawPassword rawPassword) {
        final String hashedPassword = passwordHasher.hash(rawPassword);

        return tagGenerator.generate()
                .flatMap(userTag ->
                        new User.Builder()
                                .withUsername(username)
                                .withEmail(email)
                                .withPassword(hashedPassword)
                                .withDefaultRole()
                                .withUserTag(userTag)
                                .build());
    }
}