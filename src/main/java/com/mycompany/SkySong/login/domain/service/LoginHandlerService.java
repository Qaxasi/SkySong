package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class LoginHandlerService {

    private final LoginHandler login;

    public LoginHandlerService(LoginHandler login) {
        this.login = login;
    }

    public String userLogin(LoginRequest request) {
        return login.login(request);
    }
}
