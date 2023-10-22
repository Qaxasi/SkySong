package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.dto.LoginRequest;

public interface LoginService {
    String login(LoginRequest loginRequest);
}
