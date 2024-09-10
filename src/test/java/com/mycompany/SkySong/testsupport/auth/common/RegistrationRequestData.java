package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;

public class RegistrationRequestTestData {

    public RegisterRequest validCredentials =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public RegisterRequest emptyCredentials =
            new RegisterRequest("", "", "");
    public RegisterRequest requestWithUsername(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }

    public RegisterRequest requestWithEmail(String email) {
        return new RegisterRequest("User", email, "Password#3");
    }

    public RegisterRequest requestWithPassword(String password) {
        return new RegisterRequest("User", "new@mail.com", password);
    }

    public RegisterRequest validRequest() {
        return new RegisterRequest("Alex", "alex@mail.mail", "Password#3");
    }

    public RegisterRequest request(String username, String email, String password) {
        return new RegisterRequest(username, email, password);
    }
    public String malformedRequest =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";

    public RegisterRequest passwordToShort =
            new RegisterRequest("Sam", "sam@mail.com", "Pass#3");

    public RegisterRequest passwordNoUppercaseLetter =
            new RegisterRequest("Sam", "sam@mail.com", "password#3");

    public RegisterRequest passwordNoLowercaseLetter =
            new RegisterRequest("Sam", "sam@mail.com", "PASSWORD#3");

    public RegisterRequest passwordNoSpecialCharacter =
            new RegisterRequest("Sam", "sam@mail.com", "Password3");

    public RegisterRequest passwordNoNumber =
            new RegisterRequest("Sam", "sam@mail.com", "Password#d");

    public RegisterRequest usernameToShort =
            new RegisterRequest("Sa", "sam@mail.com", "Password#3");

    public RegisterRequest usernameToLong =
            new RegisterRequest("testVeryLongUsernameFormat", "sam@mail.com", "Password#3");

    public RegisterRequest usernameWithSpecialCharacter =
            new RegisterRequest("Sam#", "sam@mail.com", "Password#3");

    public RegisterRequest emailInvalidFormat =
            new RegisterRequest("Sam", "sam-sam.com", "Password#3");

    public RegisterRequest emailToShort =
            new RegisterRequest("Sam", "s@m.c", "Password#3");

    public RegisterRequest emailToLong =
            new RegisterRequest("Sam", "qwertyuiopasdfghjklzxcvbn@mail.com", "Password#3");
}
