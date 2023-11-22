package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public interface LoginService {
    String login(LoginRequest loginRequest);
}
