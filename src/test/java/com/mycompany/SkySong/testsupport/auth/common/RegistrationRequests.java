package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class RegistrationRequests {

    public RegisterRequest validCredentials =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public RegisterRequest emptyCredentials =
            new RegisterRequest("", "", "");

    public RegisterRequest existEmail =
            new RegisterRequest("Sam", "mark@mail.com", "Password#3");
    public RegisterRequest existUsername =
            new RegisterRequest("Mark", "sam@mail.com", "Password#3");

    public RegisterRequest registerByUsername(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
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

    public RegisterRequest uniqueEmail =
            new RegisterRequest("New", "unique@mail.com", "Password#3");

    public RegisterRequest uniqueUsername =
            new RegisterRequest("Unique", "new@mail.com", "Password#3");
}
