package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.application.login.dto.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginRequests {

    public LoginRequest loginRegisteredUser =
            new LoginRequest("New", "Password#3");
    public LoginRequest validCredentials =
            new LoginRequest("User", "Password#3");
    public LoginRequest nonExistingUser =
            new LoginRequest("Invalid", "invalid");
    public LoginRequest invalidUsername =
            new LoginRequest("invalid", "Password#3");
    public LoginRequest invalidEmail =
            new LoginRequest("invalid@mail.com", "Password#3");
    public LoginRequest emptyCredentials =
            new LoginRequest("", "");


    public String malformedJson =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";

    public LoginRequest login(String username) {
        return new LoginRequest(username, "Password#3");
    }
    public LoginRequest loginInvalidPassword(String username) {
        return new LoginRequest(username, "invalid");
    }
}
