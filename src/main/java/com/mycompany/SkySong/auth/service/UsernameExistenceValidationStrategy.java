package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(4)
public class UsernameExistenceValidationStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageService message;
    private final UserDAO userDAO;

    public UsernameExistenceValidationStrategy(ApplicationMessageService message, UserDAO userDAO) {
        this.message = message;
        this.userDAO = userDAO;
    }

    @Override
    public void validate(RegisterRequest request) {
        if (userDAO.existsByUsername(request.username()) > 0) {
            throw new CredentialValidationException(message.getMessage("username.exist"));
        }
    }
}
