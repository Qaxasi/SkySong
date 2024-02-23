package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import org.springframework.security.core.Authentication;

public interface UserAuthentication {
    Authentication authenticateUser(LoginRequest loginRequest);
}
