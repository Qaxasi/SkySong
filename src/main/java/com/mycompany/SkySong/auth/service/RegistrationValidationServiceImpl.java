package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class RegistrationValidationServiceImpl implements RegistrationValidationService {

    private final List<RegistrationValidationStrategy> strategies;

    public RegistrationValidationServiceImpl(List<RegistrationValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public void validateCredentials(RegisterRequest registerRequest) {
        for (RegistrationValidationStrategy strategy : strategies) {
            strategy.validate(registerRequest);
        }
    }
}
