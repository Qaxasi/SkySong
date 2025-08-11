package com.mycompany.SkySong.identity.registration.application.validator;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.registration.application.port.RegistrationUserRepository;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public class UserRegistrationValidator {
    private final RegistrationUserRepository userRepository;

    public UserRegistrationValidator(final RegistrationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result<Void> validateFormatAndUniqueness(final UserRegistrationInput input) {
        return validateFormat(input)
                .flatMap(ignored -> validateUniqueness(input));
    }

    private Result<Void> validateFormat(final UserRegistrationInput input) {
        if (!input.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            return Result.failure("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.",
                    ErrorType.INVALID_USERNAME_FORMAT);
        }
        if (!input.email().matches("^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$")) {
            return Result.failure("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 and 30 characters long.",
                    ErrorType.INVALID_EMAIL_FORMAT);
        }
        if (!input.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            return Result.failure("Invalid password format. The password must contain an least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.",
                    ErrorType.INVALID_PASSWORD_FORMAT);
        }
        return Result.success();
    }

    private Result<Void> validateUniqueness(final UserRegistrationInput input) {
        if (userRepository.existsByUsername(input.username())) {
            return Result.failure("Username already exist!.", ErrorType.USERNAME_EXIST);
        }
        if (userRepository.existsByEmail(input.email())) {
            return Result.failure("Email already exist!.", ErrorType.EMAIL_EXIST);
        }
        return Result.success();
    }
}
