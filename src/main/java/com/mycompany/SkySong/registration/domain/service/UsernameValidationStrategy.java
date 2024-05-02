package com.mycompany.SkySong.registration.domain.validation;

import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import com.mycompany.SkySong.registration.infrastructure.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.infrastructure.util.ValidationPatterns;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class UsernameValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageLoader message;

    public UsernameValidationStrategy(ApplicationMessageLoader message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest request) throws CredentialValidationException {
        if (!request.username().matches(ValidationPatterns.USERNAME_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.username.error"));
        }
    }
}
