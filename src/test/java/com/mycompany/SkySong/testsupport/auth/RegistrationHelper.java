package com.mycompany.SkySong.testsupport.auth;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public class RegistrationHelper {
    public static final LoginRequest LOGIN =
            new LoginRequest("New", "Password#3");

    public static final RegisterRequest VALID_CREDENTIALS =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public static final RegisterRequest EMPTY_CREDENTIALS =
            new RegisterRequest("", "", "");

    public static final RegisterRequest EXIST_EMAIL =
            new RegisterRequest("User", "mark@mail.com", "Password#3");
    public static final RegisterRequest EXIST_USERNAME =
            new RegisterRequest("Mark", "mail@mail.com", "Password#3");

    public static final RegisterRequest INVALID_USERNAME =
            new RegisterRequest("invalid#", "mail@mail.com", "Password#3");
    public static final RegisterRequest INVALID_EMAIL =
            new RegisterRequest("User", "invalid", "Password#3");
    public static final RegisterRequest INVALID_PASSWORD =
            new RegisterRequest("User", "mail@mail.com", "invalid");

    public static RegisterRequest REGISTER(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }
    public static final String MALFORMED_REQUEST =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";
}
