package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class RegistrationValidationImpl implements RegistrationValidation {

    private final CredentialsValidationService validation;
    private final CredentialsExistenceChecker checker;

    public RegistrationValidationImpl(CredentialsValidationService validation,
                                      CredentialsExistenceChecker checker) {
        this.validation = validation;
        this.checker = checker;
    }

    @Override
    public void validateRequest(RegisterRequest registerRequest) {

    }
}
