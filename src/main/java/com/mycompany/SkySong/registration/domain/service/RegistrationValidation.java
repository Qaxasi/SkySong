package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationValidation {

    private final List<RegistrationValidationStrategy> strategies;

    public RegistrationValidation(List<RegistrationValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void validateRequest(RegisterRequest request) {
        for (RegistrationValidationStrategy strategy : strategies) {
            strategy.validate(request);
        }
    }
}
