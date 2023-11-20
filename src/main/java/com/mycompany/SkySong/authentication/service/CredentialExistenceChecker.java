package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;

public interface CredentialExistenceChecker {
    void checkForExistingCredentials(RegisterRequest registerRequest);
}
