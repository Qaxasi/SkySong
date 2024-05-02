package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.user.UserDAO;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(5)
public class EmailExistenceValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageLoader message;
    private final UserDAO userDAO;

    public EmailExistenceValidationStrategy(ApplicationMessageLoader message, UserDAO userDAO) {
        this.message = message;
        this.userDAO = userDAO;
    }

    @Override
    public void validate(RegisterRequest request) {
        if (userDAO.existsByEmail(request.email()) > 0) {
            throw new CredentialValidationException(message.getMessage("email.exist"));
        }
    }
}
