package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public interface RegistrationValidationService {
    void validateCredentials(RegisterRequest registerRequest);
}
