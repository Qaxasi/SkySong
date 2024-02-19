package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.utils.ValidationPatterns;
import org.springframework.stereotype.Service;

@Service
public class UsernameValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageService message;

    public UsernameValidationStrategy(ApplicationMessageService message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {
        if (!registerRequest.username().matches(ValidationPatterns.USERNAME_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.username.error"));
        }
    }
}
