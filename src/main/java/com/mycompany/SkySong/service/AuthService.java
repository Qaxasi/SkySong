package com.mycompany.SkySong.service;

import com.mycompany.SkySong.dto.LoginRequest;
import com.mycompany.SkySong.dto.RegisterRequest;
import com.mycompany.SkySong.dto.RegistrationResponse;

public interface AuthService {
    String login(LoginRequest loginRequest);
    RegistrationResponse register(RegisterRequest registerRequest);
}
