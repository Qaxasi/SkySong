package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.domain.ports.RegistrationUserRepository;

class RequestValidation {

    private final RegistrationUserRepository registrationUserRepository;

    RequestValidation(RegistrationUserRepository registrationUserRepository) {
        this.registrationUserRepository = registrationUserRepository;
    }

    void validate(RegisterRequest request) {
        if (!request.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new CredentialValidationException("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.");
        }
        if (!request.email().matches("^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$")) {
            throw new CredentialValidationException("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 and 30 characters long.");
        }
        if (!request.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new CredentialValidationException("Invalid password format. The password must contain an least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.");
        }
        if (registrationUserRepository.existsByUsername(request.username())) {
            throw new CredentialValidationException("Username is already exist!.");
        }
        if (registrationUserRepository.existsByEmail(request.email())) {
            throw new CredentialValidationException("Email is already exist!.");
        }
    }
}
