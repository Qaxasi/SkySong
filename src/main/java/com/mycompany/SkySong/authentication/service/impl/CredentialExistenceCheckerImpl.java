package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.service.CredentialExistenceChecker;
import org.springframework.stereotype.Service;

@Service
public class CredentialExistenceCheckerImpl implements CredentialExistenceChecker {
    @Override
    public void checkForExistingCredentials(RegisterRequest registerRequest) {

    }
}
