package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.dto.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse register(RegisterRequest registerRequest);
}
