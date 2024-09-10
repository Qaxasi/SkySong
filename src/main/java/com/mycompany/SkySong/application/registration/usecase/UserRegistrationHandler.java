package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.registration.mapper.UserRegistrationMapper;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.domain.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.registration.service.UserRegistrationValidator;
import com.mycompany.SkySong.domain.registration.service.UserCreator;
import com.mycompany.SkySong.domain.shared.entity.User;

public class UserRegistrationHandler {

    private final UserRegistrationValidator validation;
    private final UserCreator userCreator;
    private final UserSaver userSaver;
    private final UserRegistrationMapper mapper;

    public UserRegistrationHandler(UserRegistrationValidator validation,
                                   UserCreator userCreator,
                                   UserSaver userSaver,
                                   UserRegistrationMapper mapper) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userSaver = userSaver;
        this.mapper = mapper;
    }

    public ApiResponse registerUser(UserRegistrationDto userDto) {
        validation.validate(userDto);
        User user = userCreator.createUser(userDto);
        userSaver.saveUser(mapper.toDto(user));
        return new ApiResponse("Your registration was successful!");
    }
}
