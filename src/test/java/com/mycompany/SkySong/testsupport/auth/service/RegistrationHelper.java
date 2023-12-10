package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationHelper {
    private static RegistrationService registrationService;
    @Autowired
    public RegistrationHelper(RegistrationService registrationService) {
        RegistrationHelper.registrationService = registrationService;
    }
    public static RegisterRequest createValidRegisterRequest() {
        return new RegisterRequest("testUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public static LoginRequest userLoginRequest() {
        return new LoginRequest("testUsername", "testPassword@123");
    }
    public static void givenAndExistingUser() throws DatabaseException {
        RegisterRequest request = createValidRegisterRequest();
        registrationService.register(request);
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
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");
    }
    public static RegisterRequest createInvalidPasswordRequest() {
        return new RegisterRequest(
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");
    }
    public static RegisterRequest createExistUsernameRequest() {
        return new RegisterRequest(
                "testSecondUsername", "testEmail@gmail.com", "testPassword@123");
    }
}
