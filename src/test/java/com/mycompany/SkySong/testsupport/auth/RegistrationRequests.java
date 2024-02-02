package com.mycompany.SkySong.testsupport.auth;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public class RegistrationRequests {

    public static final RegisterRequest VALID_CREDENTIALS =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public static final RegisterRequest EMPTY_CREDENTIALS =
            new RegisterRequest("", "", "");

    public static final RegisterRequest EXIST_EMAIL =
            new RegisterRequest("Sam", "mark@mail.com", "Password#3");
    public static final RegisterRequest EXIST_USERNAME =
            new RegisterRequest("Mark", "sam@mail.com", "Password#3");

    public static final RegisterRequest INVALID_USERNAME =
            new RegisterRequest("invalid#", "sam@mail.com", "Password#3");
    public static final RegisterRequest INVALID_EMAIL =
            new RegisterRequest("Sam", "invalid", "Password#3");
    public static final RegisterRequest INVALID_PASSWORD =
            new RegisterRequest("Sam", "sam@mail.com", "invalid");

    public static RegisterRequest REGISTER(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }
    public static final String MALFORMED_REQUEST =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";
}
