package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationHelper {
    private final RegistrationService registrationService;
    public RegistrationHelper(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    public RegisterRequest createValidRegisterRequest() {
        return new RegisterRequest("testUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public LoginRequest createUserLoginRequest() {
        return new LoginRequest("testUsername", "testPassword@123");
    }
    public void executeValidUserRegistration() throws DatabaseException {
        RegisterRequest request = createValidRegisterRequest();
        registrationService.register(request);
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
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");
    }
    public RegisterRequest createInvalidPasswordRequest() {
        return new RegisterRequest(
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");
    }
    public RegisterRequest createExistUsernameRequest() {
        return new RegisterRequest(
                "testSecondUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public RegisterRequest createExistEmailRequest() {
        return new RegisterRequest(
                "testUsername", "testSecondEmail@gmail.com", "testPassword@123");
    }
}
