package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class RegistrationValidationImpl implements RegistrationValidation {

    private final List<RegistrationValidationStrategy> strategies;

    public RegistrationValidationImpl(List<RegistrationValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public void validateRequest(RegisterRequest registerRequest) {
        for (RegistrationValidationStrategy strategy : strategies) {
            strategy.validate(registerRequest);
        }
    }
}
