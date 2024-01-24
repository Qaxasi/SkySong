package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public class RegistrationRequests {
    public static final RegisterRequest VALID_CREDENTIALS =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public static final RegisterRequest INVALID_CREDENTIALS =
            new RegisterRequest("invalid", "invalid", "invalid");
    public static final RegisterRequest EMPTY_CREDENTIALS =
            new RegisterRequest("", "", "");


    public static final String MALFORMED_REQUEST =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";
}
