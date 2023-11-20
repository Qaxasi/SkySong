package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.CredentialExistenceChecker;
import org.springframework.stereotype.Service;

@Service
public class CredentialExistenceCheckerImpl implements CredentialExistenceChecker {
    private final UserDAO userDAO;
    private final ApplicationMessageService messageService;
    public CredentialExistenceCheckerImpl(UserDAO userDAO, ApplicationMessageService messageService) {
        this.userDAO = userDAO;
        this.messageService = messageService;
    }

    @Override
    public void checkForExistingCredentials(RegisterRequest registerRequest) {

    }


}
