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

    public static RegisterRequest REGISTER(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }
    public static final String MALFORMED_REQUEST =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";

    public static final RegisterRequest PASSWORD_TO_SHORT =
            new RegisterRequest("User", "mail@mail.com", "Pass#3");

    public static final RegisterRequest PASSWORD_NO_UPPERCASE_LETTER =
            new RegisterRequest("User", "mail@mail.com", "password#3");

    public static final RegisterRequest PASSWORD_NO_LOWERCASE_LETTER =
            new RegisterRequest("User", "mail@mail.com", "PASSWORD#3");

    public static final RegisterRequest PASSWORD_NO_SPECIAL_CHARACTER =
            new RegisterRequest("User", "mail@mail.com", "Password3");

    public static final RegisterRequest PASSWORD_NO_NUMBER =
            new RegisterRequest("User", "mail@mail.com", "Password#d");

    public static final RegisterRequest USERNAME_TO_SHORT =
            new RegisterRequest("Us", "mail@mail.com", "Password#3");

    public static final RegisterRequest USERNAME_TO_LONG =
            new RegisterRequest("testVeryLongUsernameFormat", "mail@mail.com", "Password#3");

    public static final RegisterRequest USERNAME_WITH_SPECIAL_CHARACTER =
            new RegisterRequest("User", "mail@mail.com", "Password#3");

    public static final RegisterRequest EMAIL_INVALID_FORMAT =
            new RegisterRequest("User", "mail-mail.com", "Password#3");

    public static final RegisterRequest EMAIL_TO_SHORT =
            new RegisterRequest("User", "m@m.c", "Password#3");

    public static final RegisterRequest EMAIL_TO_LONG =
            new RegisterRequest("User", "qwertyuiopasdfghjklzxcvbn@mail.com", "Password#3");

}
