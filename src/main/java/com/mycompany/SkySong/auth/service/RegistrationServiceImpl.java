package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationValidation validation;
    private final ApplicationMessageService messageService;
    private final UserRegistrationManager registrationManager;

    public RegistrationServiceImpl(RegistrationValidation validation,
                                   ApplicationMessageService messageService,
                                   UserRegistrationManager userRegistrationManager) {

        this.validation = validation;
        this.messageService = messageService;
        this.registrationManager = userRegistrationManager;
    }

    @Override
    public ApiResponse register(RegisterRequest request) {
        validation.validateRequest(request);
        registrationManager.setupNewUser(request);
        return new ApiResponse(messageService.getMessage("user.registration.success"));
    }
}
