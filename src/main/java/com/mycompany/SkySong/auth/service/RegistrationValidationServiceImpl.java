package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
class RegistrationValidationServiceImpl implements RegistrationValidationService {

    @Override
    public void validateCredentials(RegisterRequest registerRequest) {
    }
}
