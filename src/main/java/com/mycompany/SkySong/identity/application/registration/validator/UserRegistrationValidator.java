package com.mycompany.SkySong.identity.application.registration.validator;

import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.application.registration.exception.UserRegistrationValidationException;
import com.mycompany.SkySong.identity.application.registration.ports.RegistrationUserRepository;
import com.mycompany.SkySong.shared.error.ErrorType;

public class UserRegistrationValidator {
    private final RegistrationUserRepository userRepository;

    public UserRegistrationValidator(final RegistrationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(final UserRegistrationInput input) {
        validateFormat(input);
        validateUniqueness(input);
    }

    private void validateFormat(final UserRegistrationInput input) {
        if (!input.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new UserRegistrationValidationException("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.",
                    ErrorType.INVALID_USERNAME_FORMAT);
        }
        if (!input.email().matches("^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$")) {
            throw new UserRegistrationValidationException("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 and 30 characters long.",
                    ErrorType.INVALID_EMAIL_FORMAT);
        }
        if (!input.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new UserRegistrationValidationException("Invalid password format. The password must contain an least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.",
                    ErrorType.INVALID_PASSWORD_FORMAT);
        }
    }

    private void validateUniqueness(final UserRegistrationInput input) {
        if (userRepository.existsByUsername(input.username())) {
            throw new UserRegistrationValidationException(
                    "Username already exist!.",
                    ErrorType.USERNAME_EXIST);
        }
        if (userRepository.existsByEmail(input.email())) {
            throw new UserRegistrationValidationException(
                    "Email already exist!.",
                    ErrorType.EMAIL_EXIST);
        }
    }
}
