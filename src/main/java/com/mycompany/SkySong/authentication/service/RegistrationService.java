package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.dto.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse register(RegisterRequest registerRequest);
}
