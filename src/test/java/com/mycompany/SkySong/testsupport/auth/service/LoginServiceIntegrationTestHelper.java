package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;

public class LoginServiceIntegrationTestHelper {
    public static LoginRequest validRequest = new LoginRequest("User", "Password#3");
    public static LoginRequest invalidPassword = new LoginRequest("User", "invalid");
    public static LoginRequest invalidUsername = new LoginRequest("invalid", "Password#3");
    public static LoginRequest invalidEmail = new LoginRequest("invalid@mail.com", "Password#3");
}
