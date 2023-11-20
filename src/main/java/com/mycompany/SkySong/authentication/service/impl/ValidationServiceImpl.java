package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.service.ValidationService;
import com.mycompany.SkySong.authentication.utils.constants.ValidationPatterns;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final ApplicationMessageService messageService;

    public ValidationServiceImpl(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void validateCredentials(RegisterRequest registerRequest) {
        validateUsername(registerRequest);
        validateEmail(registerRequest);
        validatePassword(registerRequest);
    }

    private void validateUsername(RegisterRequest registerRequest) {
        if (!registerRequest.username().matches(ValidationPatterns.USERNAME_PATTERN)) {
            throw new RegisterException("Invalid username format. The username can contain only letters and numbers, " +
                    "and should be between 3 to 20 characters long.");
        }
    }

    private void validateEmail(RegisterRequest registerRequest) {
        if (!registerRequest.email().matches(ValidationPatterns.EMAIL_PATTERN)) {
            throw new RegisterException("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 to 30 characters long.");
        }
    }

    private void validatePassword(RegisterRequest registerRequest) {
        if (!registerRequest.password().matches(ValidationPatterns.PASSWORD_PATTERN)) {
            throw new RegisterException("Invalid password format. The password must contain an least 8 characters," +
                    " including uppercase letters, lowercase letters, numbers, and special characters.");
        }
    }
}
