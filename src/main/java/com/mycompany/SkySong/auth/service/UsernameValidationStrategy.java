package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;

public class UsernameValidationStrategy implements RegistrationValidationStrategy {
    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {

    }
}
