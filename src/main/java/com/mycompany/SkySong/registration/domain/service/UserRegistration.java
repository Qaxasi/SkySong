package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.dto.RegisterRequest;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;

class UserRegistration {

    private final RequestValidation validation;
    private final UserCreator userCreator;
    private final UserSaver userSaver;

    UserRegistration(RequestValidation validation, UserCreator userCreator, UserSaver userSaver) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userSaver = userSaver;
    }

    ApiResponse registerUser(RegisterRequest request) {
        validation.validate(request);
        User user = userCreator.createUser(request);
        userSaver.saveUser(user);
        return new ApiResponse("User registered successfully.");
    }
}
