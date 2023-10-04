package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.dto.LoginRequest;
import com.mycompany.SkySong.authentication.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.dto.RegistrationResponse;

public interface AuthService {
    String login(LoginRequest loginRequest);
    RegistrationResponse register(RegisterRequest registerRequest);
}
