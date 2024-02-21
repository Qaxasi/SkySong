package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public interface RegistrationValidation {
    void validateRequest(RegisterRequest registerRequest)
}
