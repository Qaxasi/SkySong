package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public class LoginRequests {
    public static final LoginRequest VALID_CREDENTIALS =
            new LoginRequest("User", "Password#3");
    public static final LoginRequest EMPTY_CREDENTIALS =
            new LoginRequest("", "");


    public static final String MALFORMED_JSON =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";
}
