package com.mycompany.SkySong.application.user.registration.usecase;

import com.mycompany.SkySong.application.user.registration.error.RegistrationExceptionMapper;
import com.mycompany.SkySong.domain.shared.exception.DomainException;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.application.user.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.application.user.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.user.registration.model.UserRegistrationData;
import com.mycompany.SkySong.domain.user.registration.service.RegistrationCredentialsValidator;
import com.mycompany.SkySong.domain.user.registration.service.UserFactory;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserRegistration {
    
    private final RegistrationCredentialsValidator validation;
    private final UserFactory userFactory;
    private final UserSaver userSaver;
    private final ApplicationLogger logger;

    public UserRegistration(final RegistrationCredentialsValidator validation,
                            final UserFactory userFactory,
                            final UserSaver userSaver,
                            final ApplicationLogger logger) {
        this.validation = validation;
        this.userFactory = userFactory;
        this.userSaver = userSaver;
        this.logger = logger;
    }

    public Result<SuccessResponse> registerUser(final UserRegistrationDto userDto) {
        try {
            final UserRegistrationData data = toDomain(userDto);

            validation.validate(data);

            final User user = userFactory.createUser(data);
            userSaver.saveUser(user);

            logger.info("User registered successfully");
            return Result.success(new SuccessResponse("Your registration was successful!"));

        } catch (DomainException ex) {
            final ErrorType errorType = RegistrationExceptionMapper.map(ex);
            logger.warn("User registration failed", context("error", errorType.name()));
            return Result.failure(ex.getMessage(), errorType);

        } catch (RuntimeException ex) {
            logger.error("Failed to save user in registration process", ex);
            throw ex;
        }
    }

    private UserRegistrationData toDomain(final UserRegistrationDto userDto) {
        return new UserRegistrationData(
                userDto.username(),
                userDto.email(),
                userDto.password());
    }
}
