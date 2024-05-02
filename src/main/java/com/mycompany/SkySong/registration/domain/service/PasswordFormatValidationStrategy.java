package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(3)
public class PasswordFormatValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageLoader message;

    public PasswordFormatValidationStrategy(ApplicationMessageLoader message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest request) throws CredentialValidationException {
        if (!request.password().matches(ValidationRegex.PASSWORD_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.password.error"));
        }
    }
}
