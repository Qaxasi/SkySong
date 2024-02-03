package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;

public class UsernameValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageService message;

    public UsernameValidationStrategy(ApplicationMessageService message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {

    }
}
