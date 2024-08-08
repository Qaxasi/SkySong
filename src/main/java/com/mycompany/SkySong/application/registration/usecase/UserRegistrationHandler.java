package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import com.mycompany.SkySong.application.registration.mapper.UserRegistrationMapper;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.domain.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.registration.service.RequestValidation;
import com.mycompany.SkySong.domain.registration.service.UserCreator;
import com.mycompany.SkySong.domain.shared.entity.User;

public class UserRegistrationHandler {

    private final RequestValidation validation;
    private final UserCreator userCreator;
    private final UserSaver userSaver;
    private final UserRegistrationMapper mapper;

    public UserRegistrationHandler(RequestValidation validation,
                                   UserCreator userCreator,
                                   UserSaver userSaver,
                                   UserRegistrationMapper mapper) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userSaver = userSaver;
        this.mapper = mapper;
    }

    public ApiResponse registerUser(RegisterRequest request) {
        validation.validate(request);
        User user = userCreator.createUser(request);
        userSaver.saveUser(mapper.toDto(user));
        return new ApiResponse("Your registration was successful!");
    }
}
