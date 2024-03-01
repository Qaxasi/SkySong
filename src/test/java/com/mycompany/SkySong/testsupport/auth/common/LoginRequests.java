package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public class LoginRequests {

    public static final LoginRequest LOGIN_REGISTERED_USER =
            new LoginRequest("New", "Password#3");
    public static final LoginRequest VALID_CREDENTIALS =
            new LoginRequest("User", "Password#3");
    public static final LoginRequest INVALID_PASSWORD =
            new LoginRequest("User", "invalid");
    public static final LoginRequest INVALID_USERNAME =
            new LoginRequest("invalid", "Password#3");
    public static final LoginRequest INVALID_EMAIL =
            new LoginRequest("invalid@mail.com", "Password#3");
    public static final LoginRequest EMPTY_CREDENTIALS =
            new LoginRequest("", "");


    public static final String MALFORMED_JSON =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";

    public static LoginRequest LOGIN(String username) {
        return new LoginRequest(username, "Password#3");
    }
    public LoginRequest loginInvalidPassword(String username) {
        return new LoginRequest(username, "invalid");
    }
}
