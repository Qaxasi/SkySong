package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;

public interface RegistrationService {
    ApiResponse register(RegisterRequest registerRequest);
}
