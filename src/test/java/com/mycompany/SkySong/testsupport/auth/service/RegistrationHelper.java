package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class RegistrationHelper {
    public RegisterRequest createValidRegisterRequest() {
        return new RegisterRequest("testUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public LoginRequest createUserLoginRequest() {
        return new LoginRequest("testUsername", "testPassword@123");
    }
    public RegisterRequest createValidRegisterRequestWithUsername(String username) {
        return new RegisterRequest(username, username + "@gmail.com", "testPassword@123");
    }
    public RegisterRequest createInvalidUsernameRequest() {
        return new RegisterRequest(
                "invalidUsername$Format", "testEmail@gmail.com", "testPassword@123");
    }
    public RegisterRequest createInvalidEmailRequest() {
        return new RegisterRequest(
                "testUsername", "invalidEmailFormat", "testPassword@123");
    }
    public RegisterRequest createInvalidPasswordRequest() {
        return new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "invalidPassword");
    }
    public RegisterRequest createExistUsernameRequest() {
        return new RegisterRequest(
                "testExistUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public RegisterRequest createExistEmailRequest() {
        return new RegisterRequest(
                "testUsername", "testExistEmail@gmail.com", "testPassword@123");
    }
}
