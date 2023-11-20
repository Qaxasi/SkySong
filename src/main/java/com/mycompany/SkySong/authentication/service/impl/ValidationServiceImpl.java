package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.service.ValidationService;
import com.mycompany.SkySong.authentication.utils.constants.ValidationPatterns;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final ApplicationMessageService messageService;

    public ValidationServiceImpl(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void validateCredentials(RegisterRequest registerRequest) {
        validateUsername(registerRequest);
        validateEmail(registerRequest);
        validatePassword(registerRequest);
    }

    private void validateUsername(RegisterRequest registerRequest) {
        if (!registerRequest.username().matches(ValidationPatterns.USERNAME_PATTERN)) {
            throw new RegisterException(messageService.getMessage("validation.username.error"));
        }
    }

    private void validateEmail(RegisterRequest registerRequest) {
        if (!registerRequest.email().matches(ValidationPatterns.EMAIL_PATTERN)) {
            throw new RegisterException(messageService.getMessage("validation.email.error"));
        }
    }

    private void validatePassword(RegisterRequest registerRequest) {
        if (!registerRequest.password().matches(ValidationPatterns.PASSWORD_PATTERN)) {
            throw new RegisterException(messageService.getMessage("validation.password.error"));
        }
    }
}
