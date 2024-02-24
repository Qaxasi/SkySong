package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationValidation validation;
    private final ApplicationMessageService messageService;
    private final UserCreation userCreation;

    public RegistrationServiceImpl(RegistrationValidation validation,
                                   ApplicationMessageService messageService,
                                   UserCreation userCreation) {

        this.validation = validation;
        this.messageService = messageService;
        this.userCreation = userCreation;
    }

    @Transactional
    @Override
    public ApiResponse register(RegisterRequest registerRequest) {
        validation.validateRequest(registerRequest);
        userCreation.createUser(registerRequest);
        return new ApiResponse(messageService.getMessage("user.registration.success"));
    }
}
