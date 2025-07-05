package com.mycompany.SkySong.identity.application.registration.service;

import com.mycompany.SkySong.identity.adapter.out.db.exception.UserSaverPersistenceException;
import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.identity.application.registration.ports.UserSaver;
import com.mycompany.SkySong.identity.application.registration.validator.UserRegistrationValidator;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserRegistration {
    private final UserRegistrationValidator validation;
    private final UserFactory userFactory;
    private final UserSaver userSaver;
    private final ApplicationLogger logger;

    public UserRegistration(final UserRegistrationValidator validation,
                            final UserFactory userFactory,
                            final UserSaver userSaver,
                            final ApplicationLogger logger) {
        this.validation = validation;
        this.userSaver = userSaver;
        this.userFactory = userFactory;
        this.logger = logger;
    }

    public Result<SuccessResponse> execute(final UserRegistrationInput input) {
        return validateRequiredFieldsPresent(input)
                .flatMap(ignored -> validation.validateFormatAndUniqueness(input))
                .flatMap(ignored -> userFactory.createUser(input))
                .flatMap(user -> saveUser(user))
                .map(ignored -> new SuccessResponse("Your registration was successful!"));
    }

    private Result<Void> saveUser(final User user) {
        try {
            userSaver.saveUser(user);
        } catch (UserSaverPersistenceException e) {
            logger.error("Persistence error while saving user", e, context("userId", user.getId()));
            return Result.failure("Failed to register user due to persistence error", ErrorType.PERSISTENCE_ERROR);
        }
        return Result.success();
    }

    private Result<Void> validateRequiredFieldsPresent(final UserRegistrationInput input) {
        if (input.username() == null || input.username().isBlank()
                || input.email() == null || input.email().isBlank()
                || input.password() == null || input.password().isBlank()) {
            return Result.failure(
                    "Missing required registration fields",
                    ErrorType.INVALID_REGISTRATION_INPUT);
        }
        return Result.success();
    }
}
