package com.mycompany.SkySong.service;

import com.mycompany.SkySong.user.entity.LoginRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
}
