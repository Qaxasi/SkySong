package com.mycompany.SkySong.identity.registration.application.validator;

import com.mycompany.SkySong.identity.registration.application.dto.UniquenessStatus;
import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationData;
import com.mycompany.SkySong.identity.registration.application.port.RegistrationUserRepository;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public class UserRegistrationValidator {
    private final RegistrationUserRepository userRepository;

    public UserRegistrationValidator(final RegistrationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result<Void> validateFormatAndUniqueness(final UserRegistrationData data) {
        return validateFormat(data)
                .flatMap(ignored -> validateUniqueness(data));
    }

    private Result<Void> validateFormat(final UserRegistrationData data) {
        if (!data.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            return Result.failure("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.",
                    ErrorType.VALIDATION_ERROR);
        }
        if (!data.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            return Result.failure("Invalid password format. The password must contain at least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.",
                    ErrorType.VALIDATION_ERROR);
        }
        return Result.success();
    }

    private Result<Void> validateUniqueness(final UserRegistrationData data) {
        final UniquenessStatus status = userRepository.checkUniqueness(data.username(), data.email());
        if (status.usernameExists() && status.emailExists()) {
            return Result.failure("Username and email already exists", ErrorType.VALIDATION_ERROR);
        }
        if (status.usernameExists()) {
            return Result.failure("Username already exists", ErrorType.VALIDATION_ERROR);
        }
        if (status.emailExists()) {
            return Result.failure("Email already exists", ErrorType.VALIDATION_ERROR);
        }
        return Result.success();
    }
}
