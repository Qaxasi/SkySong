package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class EmailExistStrategy implements RegistrationValidationStrategy {

    private final ApplicationMessageService message;
    private final UserDAO userDAO;

    public EmailExistStrategy(ApplicationMessageService message, UserDAO userDAO) {
        this.message = message;
        this.userDAO = userDAO;
    }

    @Override
    public void validate(RegisterRequest registerRequest) {
        if (userDAO.existsByEmail(registerRequest.email()) > 0) {
            throw new CredentialValidationException(message.getMessage("email.exist"));
        }
    }
}
