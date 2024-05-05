package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;

class UserRegistrationService {

    private final UserRegistration registration;
    private final RequestValidation validation;

    UserRegistrationService(UserRegistration registration, RequestValidation validation) {
        this.registration = registration;
        this.validation = validation;
    }

    ApiResponse validateAndRegisterUser(RegisterRequest request) {
        validation.validate(request);
        return registration.registerUser(request);
    }
}
