package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.xyz.UserRegistrationMapper;
import com.mycompany.SkySong.registration.dto.RegisterRequest;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;

public class UserRegistration {

    private final RequestValidation validation;
    private final UserCreator userCreator;
    private final UserRegistrationMapper userMapper;
    private final UserSaver userSaver;

    UserRegistration(RequestValidation validation,
                     UserCreator userCreator,
                     UserRegistrationMapper userRegistrationMapper,
                     UserSaver userSaver) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userMapper = userRegistrationMapper;
        this.userSaver = userSaver;
    }

    public ApiResponse registerUser(RegisterRequest request) {
        validation.validate(request);
        User user = userCreator.createUser(request);
        userSaver.saveUser(userMapper.toDto(user));
        return new ApiResponse("User registered successfully.");
    }
}
