package com.mycompany.SkySong.testsupport.auth;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public class LoginRequests {
    public static LoginRequest VALID_CREDENTIALS =
            new LoginRequest("User", "Password#3");
    public static LoginRequest INVALID_PASSWORD =
            new LoginRequest("User", "invalid");
    public static LoginRequest INVALID_USERNAME =
            new LoginRequest("invalid", "Password#3");
    public static LoginRequest INVALID_EMAIL =
            new LoginRequest("invalid@mail.com", "Password#3");
    public static LoginRequest EMPTY_CREDENTIALS =
            new LoginRequest("", "");


    public static String MALFORMED_JSON =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";
}
