package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public class LoginRequests {

    public static final LoginRequest loginRegisteredUser =
            new LoginRequest("New", "Password#3");
    public static final LoginRequest validCredentials =
            new LoginRequest("User", "Password#3");
    public static final LoginRequest invalidPassword =
            new LoginRequest("User", "invalid");
    public static final LoginRequest invalidUsername =
            new LoginRequest("invalid", "Password#3");
    public static final LoginRequest invalidEmail =
            new LoginRequest("invalid@mail.com", "Password#3");
    public static final LoginRequest emptyCredentials =
            new LoginRequest("", "");


    public static final String malformedJson =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";

    public static LoginRequest login(String username) {
        return new LoginRequest(username, "Password#3");
    }
    public LoginRequest loginInvalidPassword(String username) {
        return new LoginRequest(username, "invalid");
    }
}
