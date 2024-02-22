package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class EmailExistStrategy implements RegistrationValidationStrategy {
    @Override
    public void validate(RegisterRequest registerRequest) throws CredentialValidationException {
        
    }
}
