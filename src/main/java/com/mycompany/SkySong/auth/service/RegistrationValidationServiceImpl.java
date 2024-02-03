package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.shared.util.ValidationPatterns;
import org.springframework.stereotype.Service;

@Service
class RegistrationValidationServiceImpl implements RegistrationValidationService {
    private final ApplicationMessageService messageService;

    public RegistrationValidationServiceImpl(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void validateCredentials(RegisterRequest registerRequest) {
        validateUsername(registerRequest);
        validateEmail(registerRequest);
        validatePassword(registerRequest);
    }
}
