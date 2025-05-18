package com.mycompany.SkySong.domain.user.registration.service;

import com.mycompany.SkySong.domain.user.registration.exception.*;
import com.mycompany.SkySong.domain.user.registration.model.UserRegistrationData;
import com.mycompany.SkySong.domain.user.registration.ports.RegistrationUserRepository;

public class RegistrationCredentialsValidator {
    private final RegistrationUserRepository registrationUserRepository;

    public RegistrationCredentialsValidator(final RegistrationUserRepository registrationUserRepository) {
        this.registrationUserRepository = registrationUserRepository;
    }

    public void validate(final UserRegistrationData data) {
        if (!data.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new InvalidUsernameFormat("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.");
        }
        if (!data.email().matches("^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$")) {
            throw new InvalidEmailFormat("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 and 30 characters long.");
        }
        if (!data.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new InvalidPasswordFormat("Invalid password format. The password must contain an least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.");
        }
        if (registrationUserRepository.existsByUsername(data.username())) {
            throw new UsernameAlreadyExist("Username already exist!.");
        }
        if (registrationUserRepository.existsByEmail(data.email())) {
            throw new EmailAlreadyExist("Email already exist!.");
        }
    }
}
