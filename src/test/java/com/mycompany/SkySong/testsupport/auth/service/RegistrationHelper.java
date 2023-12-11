package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;

public class RegistrationHelper {
    public static RegisterRequest createValidRegisterRequest() {
        return new RegisterRequest("testUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public static LoginRequest createUserLoginRequest() {
        return new LoginRequest("testUsername", "testPassword@123");
    }
    public static RegisterRequest createValidRegisterRequestWithUsername(String username) {
        return new RegisterRequest(username, username + "@gmail.com", "testPassword@123");
    }
    public static RegisterRequest createInvalidUsernameRequest() {
        return new RegisterRequest(
                "invalidUsername$Format", "testEmail@gmail.com", "testPassword@123");
    }
    public static RegisterRequest createInvalidEmailRequest() {
        return new RegisterRequest(
                "testUsername", "invalidEmailFormat", "testPassword@123");
    }
    public static RegisterRequest createInvalidPasswordRequest() {
        return new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "invalidPassword");
    }
    public static RegisterRequest createExistUsernameRequest() {
        return new RegisterRequest(
                "testExistUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public static RegisterRequest createExistEmailRequest() {
        return new RegisterRequest(
                "testUsername", "testExistEmail@gmail.com", "testPassword@123");
    }
    public static void executeValidUserRegistration(RegistrationService registrationService) throws DatabaseException {
        registrationService.register(createValidRegisterRequest());
    }
}
