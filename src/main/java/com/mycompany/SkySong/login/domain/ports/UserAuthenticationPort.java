package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.login.application.dto.LoginRequest;
import org.springframework.security.core.Authentication;

public interface UserAuthenticationPort {
    Authentication authenticateUser(LoginRequest request);
}
