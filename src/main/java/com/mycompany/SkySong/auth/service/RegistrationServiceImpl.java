package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationValidation validation;
    private final ApplicationMessageLoader message;
    private final UserRegistrationManager registrationManager;

    public RegistrationServiceImpl(RegistrationValidation validation,
                                   ApplicationMessageLoader message,
                                   UserRegistrationManager userRegistrationManager) {

        this.validation = validation;
        this.message = message;
        this.registrationManager = userRegistrationManager;
    }

    @Override
    public ApiResponse register(RegisterRequest request) {
        validation.validateRequest(request);
        registrationManager.setupNewUser(request);
        return new ApiResponse(message.getMessage("user.registration.success"));
    }
}
