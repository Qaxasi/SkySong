package com.mycompany.SkySong.identity.registration.application.service;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationInput;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.identity.registration.application.port.UserSaver;
import com.mycompany.SkySong.identity.registration.application.validator.UserRegistrationValidator;
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
                .flatMap(ignored -> validation.validateFormatAndUniqueness(input)
                        .onFailure(error -> logger.warn("Registration data validation failed", context("errorType", error.errorType()))))
                .flatMap(ignored -> userFactory.createUser(input)
                        .onFailure(error -> logger.warn("User creation failed", context("errorType", error.errorType()))))
                .flatMap(user -> userSaver.saveUser(user))
                .map(ignored -> new SuccessResponse("Your registration was successful!"));
    }



    private Result<Void> validateRequiredFieldsPresent(final UserRegistrationInput input) {
        if (input.username() == null || input.username().isBlank()
                || input.email() == null || input.email().isBlank()
                || input.password() == null || input.password().isBlank()) {
            logger.warn("Registration failed - missing required fields");
            return Result.failure(
                    "Missing required registration fields",
                    ErrorType.MISSING_REQUIRED_FIELD);
        }
        return Result.success();
    }
}