package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import org.springframework.stereotype.Component;

@Component
public class UserLoginHelper {

    private final LoginService login;
    private final LoginRequests loginHelper;

    public UserLoginHelper(LoginService login, LoginRequests loginHelper) {
        this.login = login;
        this.loginHelper = loginHelper;
    }

    public String loginRegisteredUser() {
        return login.login(loginHelper.loginRegisteredUser);
    }
}
