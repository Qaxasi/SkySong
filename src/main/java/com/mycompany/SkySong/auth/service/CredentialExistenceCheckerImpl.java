package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.repository.UserDAO;
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
        if (userDAO.existsByUsername(registerRequest.username()) > 0) {
            throw new CredentialValidationException(messageService.getMessage("username.exist"));
        }
    }
    private void checkForExistingEmail(RegisterRequest registerRequest) {
        if (userDAO.existsByEmail(registerRequest.email()) > 0) {
            throw new CredentialValidationException(messageService.getMessage("email.exist"));
        }
    }
}
