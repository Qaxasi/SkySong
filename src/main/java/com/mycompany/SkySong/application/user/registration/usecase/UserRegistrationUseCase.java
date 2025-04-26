package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.registration.mapper.UserSaveMapper;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.registration.ports.UserSaver;
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

    public UserRegistrationUseCase(UserRegistrationValidator validation,
                                   UserCreator userCreator,
                                   UserSaver userSaver,
                                   UserSaveMapper mapper) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userSaver = userSaver;
        this.mapper = mapper;
    }

    public Result<ApiResponse> registerUser(UserRegistrationDto userDto) {
        try {
            UserRegistrationData data = new UserRegistrationData(
                    userDto.username(),
                    userDto.email(), userDto.
                    password());
            validation.validate(data);
            User user = userCreator.createUser(data);
            userSaver.saveUser(mapper.toDto(user));
            return Result.success(new ApiResponse("Your registration was successful!"));
        } catch (BaseApiException ex) {
            return Result.failure(ex.getMessage(), ex.getErrorType());
        }
    }
}
