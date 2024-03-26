package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {

    private final RegistrationValidation validation;
    private final ApplicationMessageLoader message;
    private final UserRegistration registration;

    public RegistrationService(RegistrationValidation validation,
                               ApplicationMessageLoader message,
                               UserRegistration registration) {

        this.validation = validation;
        this.message = message;
        this.registration = registration;
    }

    public ApiResponse register(RegisterRequest request) {
        validation.validateRequest(request);
        registration.registerUser(request);
        return new ApiResponse(message.getMessage("user.registration.success"));
    }
}
