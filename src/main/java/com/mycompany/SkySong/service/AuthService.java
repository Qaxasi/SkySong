package com.mycompany.SkySong.service;

import com.mycompany.SkySong.user.entity.LoginRequest;
import com.mycompany.SkySong.user.entity.RegisterRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
    String register(RegisterRequest registerRequest);
}
