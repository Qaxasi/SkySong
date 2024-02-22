package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.utils.ValidationPatterns;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(3)
public class PasswordValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageService message;

    public PasswordValidationStrategy(ApplicationMessageService message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {
        if (!registerRequest.password().matches(ValidationPatterns.PASSWORD_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.password.error"));
        }
    }
}
