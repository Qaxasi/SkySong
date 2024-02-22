package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class UsernameExistenceValidationStrategy implements RegistrationValidationStrategy {
    
    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {

    }
}
