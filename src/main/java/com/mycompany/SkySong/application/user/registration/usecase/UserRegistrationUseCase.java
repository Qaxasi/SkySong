package com.mycompany.SkySong.application.user.registration.usecase;

import com.mycompany.SkySong.application.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.application.user.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.user.registration.mapper.UserSaveMapper;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.user.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.user.registration.model.UserRegistrationData;
import com.mycompany.SkySong.domain.user.registration.service.UserRegistrationValidator;
import com.mycompany.SkySong.domain.user.registration.service.UserCreator;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.result.Result;

public class UserRegistrationUseCase {

    private final UserRegistrationValidator validation;
    private final UserCreator userCreator;
    private final UserSaver userSaver;
    private final UserSaveMapper mapper;
    private final ApplicationLogger logger;

    public UserRegistrationUseCase(final UserRegistrationValidator validation,
                                   final UserCreator userCreator,
                                   final UserSaver userSaver,
                                   final UserSaveMapper mapper,
                                   final ApplicationLogger logger) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userSaver = userSaver;
        this.mapper = mapper;
        this.logger = logger;
    }

    public Result<ApiResponse> registerUser(final UserRegistrationDto userDto) {
        try {
            final UserRegistrationData data = toDomain(userDto);

            validation.validate(data);

            final User user = userCreator.createUser(data);
            userSaver.saveUser(mapper.toDto(user));

            logger.info("User registered successfully");
            return Result.success(new ApiResponse("Your registration was successful!"));
        } catch (BaseApiException ex) {
            logger.warn("User registration failed",
                    ApplicationLogger.Context.of(
                   "error", ex.getErrorType().name()));
            return Result.failure(ex.getMessage(), ex.getErrorType());
        } catch (RuntimeException ex) {
            logger.error("Unexpected technical error during user registration", ex);
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
