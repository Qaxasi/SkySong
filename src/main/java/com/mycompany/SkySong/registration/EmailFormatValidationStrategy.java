package com.mycompany.SkySong.registration;

import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class EmailFormatValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageLoader message;

    public EmailFormatValidationStrategy(ApplicationMessageLoader message) {
        this.message = message;
    }

    @Override
    public void validate(RegisterRequest request) throws CredentialValidationException {
        if (!request.email().matches(ValidationPatterns.EMAIL_PATTERN)) {
            throw new CredentialValidationException(message.getMessage("validation.email.error"));
        }
    }
}
