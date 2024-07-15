package com.mycompany.SkySong.domain.registration.service;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.registration.mapper.UserRegistrationMapper;
import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.registration.ports.UserSaver;

public class UserRegistration {
    private final RequestValidation validation;
    private final UserCreator userCreator;

    UserRegistration(RequestValidation validation,
                     UserCreator userCreator) {
        this.validation = validation;
        this.userCreator = userCreator;
    }

    public User registerUser(RegisterRequest request) {
        validation.validate(request);
        return userCreator.createUser(request);
    }
}
