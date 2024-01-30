package com.mycompany.SkySong.testsupport.auth;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public class RegistrationHelper {
    public LoginRequest login =
            new LoginRequest("User", "Password#3");

    public RegisterRequest validCredentials =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public RegisterRequest emptyCredentials =
            new RegisterRequest("", "", "");

    public RegisterRequest existEmail =
            new RegisterRequest("User", "mark@mail.com", "Password#3");
    public RegisterRequest existUsername =
            new RegisterRequest("Mark", "mail@mail.com", "Password#3");

    public RegisterRequest invalidUsername =
            new RegisterRequest("invalid#", "mail@mail.com", "Password#3");
    public RegisterRequest invalidEmail =
            new RegisterRequest("User", "invalid", "Password#3");
    public RegisterRequest invalidPassword =
            new RegisterRequest("User", "mail@mail.com", "invalid");

    public RegisterRequest register(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }
    public String malformedRequest =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";
}
