package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import org.springframework.stereotype.Component;

@Component
public class UserLoginHelper {

    private final LoginService login;
    private final LoginRequests request;

    public UserLoginHelper(LoginService login, LoginRequests request) {
        this.login = login;
        this.request = request;
    }

    public String loginRegisteredUser() {
        return login.login(LoginRequests.LOGIN_REGISTERED_USER);
    }
}
