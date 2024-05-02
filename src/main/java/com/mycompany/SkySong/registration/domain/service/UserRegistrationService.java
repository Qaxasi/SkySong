package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegistrationService {

    private final RegistrationValidation validation;
    private final ApplicationMessageLoader message;
    private final UserRegistration registration;

    public UserRegistrationService(RegistrationValidation validation,
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
