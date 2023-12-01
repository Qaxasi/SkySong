package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.springframework.stereotype.Service;

@Service
class CredentialExistenceCheckerImpl implements CredentialExistenceChecker {
    private final UserDAO userDAO;
    private final ApplicationMessageService messageService;
    public CredentialExistenceCheckerImpl(UserDAO userDAO, ApplicationMessageService messageService) {
        this.userDAO = userDAO;
        this.messageService = messageService;
    }
    @Override
    public void checkForExistingCredentials(RegisterRequest registerRequest) {
        checkForExistingUsername(registerRequest);
        checkForExistingEmail(registerRequest);
    }
    private void checkForExistingUsername(RegisterRequest registerRequest) {
        if (userDAO.existsByUsername(registerRequest.username())) {
            throw new CredentialValidationException(messageService.getMessage("username.exist"));
        }
    }
    private void checkForExistingEmail(RegisterRequest registerRequest) {
        if (userDAO.existsByEmail(registerRequest.email())) {
            throw new CredentialValidationException(messageService.getMessage("email.exist"));
        }
    }
}
