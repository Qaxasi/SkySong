package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.utils.ValidationPatterns;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class EmailValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageLoader message;

    public EmailValidationStrategy(ApplicationMessageLoader message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest request) throws CredentialValidationException {
        if (!request.email().matches(ValidationPatterns.EMAIL_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.email.error"));
        }
    }
}
