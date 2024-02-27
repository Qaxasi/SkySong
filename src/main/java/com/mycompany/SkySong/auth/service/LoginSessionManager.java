package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public interface LoginSessionManager {
    String initializeAndSaveSession(LoginRequest request);
}
