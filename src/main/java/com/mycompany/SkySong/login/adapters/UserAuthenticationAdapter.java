package com.mycompany.SkySong.login.adapters;

import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.login.domain.ports.UserAuthenticationPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
class UserAuthenticationAdapter implements UserAuthenticationPort {

    private final AuthenticationManager authManager;

    UserAuthenticationAdapter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication authenticateUser(LoginRequest request) {
        return null;
    }
}
