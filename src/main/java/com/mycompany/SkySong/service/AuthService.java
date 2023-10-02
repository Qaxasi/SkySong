package com.mycompany.SkySong.service;

import com.mycompany.SkySong.dto.LoginRequest;
import com.mycompany.SkySong.dto.RegisterRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
    String register(RegisterRequest registerRequest);
}
