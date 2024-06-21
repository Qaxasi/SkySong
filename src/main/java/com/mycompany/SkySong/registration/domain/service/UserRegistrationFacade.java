package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.dto.RegisterRequest;

public class UserRegistrationFacade {
    private final UserRegistration registration;

    public UserRegistrationFacade(UserRegistration registration) {
        this.registration = registration;
    }

    public ApiResponse register(RegisterRequest request) {
        return registration.registerUser(request);
    }
}
