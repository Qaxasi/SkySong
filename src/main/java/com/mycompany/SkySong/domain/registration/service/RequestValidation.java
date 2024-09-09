package com.mycompany.SkySong.domain.registration.service;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.domain.registration.exception.CredentialValidationException;
import com.mycompany.SkySong.domain.registration.ports.RegistrationUserRepository;

public class RequestValidation {

    private final RegistrationUserRepository registrationUserRepository;

    public RequestValidation(RegistrationUserRepository registrationUserRepository) {
        this.registrationUserRepository = registrationUserRepository;
    }

    public void validate(UserRegistrationDto userDto) {
        if (!userDto.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new CredentialValidationException("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.");
        }
        if (!userDto.email().matches("^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$")) {
            throw new CredentialValidationException("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 and 30 characters long.");
        }
        if (!userDto.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new CredentialValidationException("Invalid password format. The password must contain an least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.");
        }
        if (registrationUserRepository.existsByUsername(userDto.username())) {
            throw new CredentialValidationException("Username is already exist!.");
        }
        if (registrationUserRepository.existsByEmail(userDto.email())) {
            throw new CredentialValidationException("Email is already exist!.");
        }
    }
}
