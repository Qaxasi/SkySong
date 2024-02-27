package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import org.springframework.stereotype.Component;

@Component
public class UserLoginTestHelper {

    private final LoginService login;

    public UserLoginTestHelper(LoginService login) {
        this.login = login;
    }

    public String loginRegisteredUser() {
        return login.login(LoginRequests.LOGIN_REGISTERED_USER);
    }
}
