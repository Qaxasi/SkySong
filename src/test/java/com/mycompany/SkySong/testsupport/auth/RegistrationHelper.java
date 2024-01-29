package com.mycompany.SkySong.testsupport.auth;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public class RegistrationHelper {
    private static final String password = "Password#1";
    public static RegisterRequest register = new RegisterRequest("User", "mail@mail.com", password);
    public static LoginRequest login = new LoginRequest("User", password);

    public static RegisterRequest invalidUsername = new RegisterRequest(
            "invalid#", "mail@mail.com", password);
    public static RegisterRequest invalidEmail = new RegisterRequest(
            "User", "invalid", password);
    public static RegisterRequest invalidPassword = new RegisterRequest(
            "User", "mail@mail.com", "invalid");

    public static RegisterRequest existingUsername = new RegisterRequest(
            "Mark", "mail@mail.com", password);
    public static RegisterRequest existEmail = new RegisterRequest(
            "User", "mark@mail.com", password);
    public static RegisterRequest register(String username) {
        return new RegisterRequest(username, "mail@mail.com", password);
    }
}
