package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserRegistration registration;

    public UserRegistrationService(UserRegistration registration) {
        this.registration = registration;
    }

    public ApiResponse register(RegisterRequest request) {
        return registration.registerUser(request);
    }
}
