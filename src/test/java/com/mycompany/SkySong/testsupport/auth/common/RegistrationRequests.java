package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public class RegistrationRequests {

    public RegisterRequest VALID_CREDENTIALS =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public RegisterRequest EMPTY_CREDENTIALS =
            new RegisterRequest("", "", "");

    public RegisterRequest EXIST_EMAIL =
            new RegisterRequest("Sam", "mark@mail.com", "Password#3");
    public RegisterRequest EXIST_USERNAME =
            new RegisterRequest("Mark", "sam@mail.com", "Password#3");

    public RegisterRequest REGISTER(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }
    public String MALFORMED_REQUEST =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";

    public RegisterRequest PASSWORD_TO_SHORT =
            new RegisterRequest("Sam", "sam@mail.com", "Pass#3");

    public RegisterRequest PASSWORD_NO_UPPERCASE_LETTER =
            new RegisterRequest("Sam", "sam@mail.com", "password#3");

    public RegisterRequest PASSWORD_NO_LOWERCASE_LETTER =
            new RegisterRequest("Sam", "sam@mail.com", "PASSWORD#3");

    public RegisterRequest PASSWORD_NO_SPECIAL_CHARACTER =
            new RegisterRequest("Sam", "sam@mail.com", "Password3");

    public RegisterRequest PASSWORD_NO_NUMBER =
            new RegisterRequest("Sam", "sam@mail.com", "Password#d");

    public RegisterRequest USERNAME_TO_SHORT =
            new RegisterRequest("Sa", "sam@mail.com", "Password#3");

    public RegisterRequest USERNAME_TO_LONG =
            new RegisterRequest("testVeryLongUsernameFormat", "sam@mail.com", "Password#3");

    public RegisterRequest USERNAME_WITH_SPECIAL_CHARACTER =
            new RegisterRequest("Sam#", "sam@mail.com", "Password#3");

    public RegisterRequest EMAIL_INVALID_FORMAT =
            new RegisterRequest("Sam", "sam-sam.com", "Password#3");

    public RegisterRequest EMAIL_TO_SHORT =
            new RegisterRequest("Sam", "s@m.c", "Password#3");

    public RegisterRequest EMAIL_TO_LONG =
            new RegisterRequest("Sam", "qwertyuiopasdfghjklzxcvbn@mail.com", "Password#3");

    public RegisterRequest UNIQUE_EMAIL =
            new RegisterRequest("New", "unique@mail.com", "Password#3");

    public RegisterRequest UNIQUE_USERNAME =
            new RegisterRequest("Unique", "new@mail.com", "Password#3");
}
