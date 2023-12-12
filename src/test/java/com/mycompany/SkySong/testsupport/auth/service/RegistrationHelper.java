package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;

public class RegistrationHelper {
    private final String password = "Password#1";
    public static RegisterRequest register = new RegisterRequest("User", "mail@mail", password);
    public LoginRequest login = new LoginRequest("User", password);

    public RegisterRequest invalidUsername = new RegisterRequest("invalid#", "mail@mail", password);
    public RegisterRequest invalidEmail = new RegisterRequest("User", "invalid", password);
    public RegisterRequest invalidPassword = new RegisterRequest("User", "mail@mail", "invalid");

    public RegisterRequest existingUsername = new RegisterRequest(
            "existUsername", "mail@mail", password);
    public RegisterRequest existEmail = new RegisterRequest(
            "User", "existMail@mail", password);
    public RegisterRequest register(String username) {
        return new RegisterRequest(username, "mail@mail", password);
    }
}
