package com.mycompany.SkySong.registration.domain.validation;

import com.mycompany.SkySong.registration.infrastructure.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;

public interface RegistrationValidationStrategy {
    void validate(RegisterRequest registerRequest) throws CredentialValidationException;
}
