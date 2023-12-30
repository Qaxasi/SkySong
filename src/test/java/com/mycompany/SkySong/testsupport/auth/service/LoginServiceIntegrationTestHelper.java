package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.service.LoginService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginServiceIntegrationTestHelper {
    public static LoginRequest validRequest = new LoginRequest("User", "Password#3");
    public static LoginRequest invalidPassword = new LoginRequest("User", "invalid");
    public static LoginRequest invalidUsername = new LoginRequest("invalid", "Password#3");
    public static LoginRequest invalidEmail = new LoginRequest("invalid@mail.com", "Password#3");
    public static void assertUserAuthWithUsername(String username, LoginService login) {
        login.login(validRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(username, authentication.getName());
    }
    public static void assertLoginFailureWithMessage(LoginService login, LoginRequest request,
                                                     Class<? extends Exception> expectedException, String message) {
        Exception exception = assertThrows(expectedException, () -> login.login(request));
        assertEquals(message, exception.getMessage());
    }
}
