package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class EmailFormatValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageLoader message;

    public EmailFormatValidationStrategy(ApplicationMessageLoader message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest request) throws CredentialValidationException {
        if (!request.email().matches(ValidationRegex.EMAIL_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.email.error"));
        }
    }
}
