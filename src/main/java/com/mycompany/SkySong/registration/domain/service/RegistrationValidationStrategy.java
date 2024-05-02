package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;

public interface RegistrationValidationStrategy {
    void validate(RegisterRequest registerRequest) throws CredentialValidationException;
}
