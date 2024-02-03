package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;

public class EmailValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageService message;

    public EmailValidationStrategy(ApplicationMessageService message) {
        this.message = message;
    }
    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {

    }
}
