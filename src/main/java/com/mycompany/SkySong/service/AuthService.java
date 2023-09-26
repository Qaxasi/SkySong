package com.mycompany.SkySong.service;

import com.mycompany.SkySong.user.entity.LoginResponse;

public interface AuthService {
    String login(LoginResponse loginResponse);
}
