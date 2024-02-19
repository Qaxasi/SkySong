package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.exception.CredentialValidationException;

public interface RegistrationValidationStrategy {
    void validate(RegisterRequest registerRequest) throws CredentialValidationException;
}
