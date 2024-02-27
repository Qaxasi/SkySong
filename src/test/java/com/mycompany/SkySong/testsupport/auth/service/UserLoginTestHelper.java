package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.service.LoginService;
import org.springframework.stereotype.Component;

@Component
public class UserLoginTestHelper {

    private final LoginService login;

    public UserLoginTestHelper(LoginService login) {
        this.login = login;
    }
}
