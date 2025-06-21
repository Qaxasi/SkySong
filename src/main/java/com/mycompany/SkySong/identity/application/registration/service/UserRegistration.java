package com.mycompany.SkySong.identity.application.registration.service;

import com.mycompany.SkySong.identity.application.exception.IdentityApplicationException;
import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
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
        this.userFactory = userFactory;
        this.userSaver = userSaver;
        this.logger = logger;
    }

    public Result<SuccessResponse> execute(final UserRegistrationInput input) {
        try {
            validation.validate(input);

            final User user = userFactory.createUser(input);
            userSaver.saveUser(user);

            logger.info("User registered successfully");
            return Result.success(new SuccessResponse("Your registration was successful!"));

        } catch (IdentityApplicationException ex) {
            logger.warn("User registration failed", context("error", ex.getErrorType().name()));
            return Result.failure(ex.getMessage(), ex.getErrorType());
        }
    }
}
