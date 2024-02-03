package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;

public interface ValidationStrategy {
    void validate(RegisterRequest registerRequest) throws CredentialValidationException;
}
