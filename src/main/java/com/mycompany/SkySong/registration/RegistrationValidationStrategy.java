package com.mycompany.SkySong.registration;

public interface RegistrationValidationStrategy {
    void validate(RegisterRequest registerRequest) throws CredentialValidationException;
}
