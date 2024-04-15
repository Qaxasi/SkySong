package com.mycompany.SkySong.registration;

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
