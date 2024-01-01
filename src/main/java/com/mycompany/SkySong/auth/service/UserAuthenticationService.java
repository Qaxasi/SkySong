package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {
    Authentication authenticateUser(LoginRequest loginRequest);
}
