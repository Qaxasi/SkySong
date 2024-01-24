package com.mycompany.SkySong.testsupport;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenGeneratorHelper {
    @Autowired
    private LoginService login;
    public String generateCorrectToken() {
        return login.login(new LoginRequest("mail@mail.com", "Password#3"));
    }
}
