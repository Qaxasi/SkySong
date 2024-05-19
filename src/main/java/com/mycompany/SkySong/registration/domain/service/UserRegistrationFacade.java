package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationFacade {

    private final UserRegistrationHandler registration;

    public UserRegistrationFacade(UserRegistrationHandler registration) {
        this.registration = registration;
    }

    public ApiResponse register(RegisterRequest request) {
        return registration.validateAndRegisterUser(request);
    }
}
