package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CredentialExistenceCheckerImpl implements CredentialExistenceChecker {
    private final UserDAO userDAO;
    private final ApplicationMessageServiceImpl messageService;
    public CredentialExistenceCheckerImpl(UserDAO userDAO, ApplicationMessageServiceImpl messageService) {
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
            throw new RegisterException(messageService.getMessage("username.exist"));
        }
    }
    private void checkForExistingEmail(RegisterRequest registerRequest) {
        if (userDAO.existsByEmail(registerRequest.email())) {
            throw new RegisterException(messageService.getMessage("email.exist"));
        }
    }
}
