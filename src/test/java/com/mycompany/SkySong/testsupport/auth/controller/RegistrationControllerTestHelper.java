package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.exception.RegisterException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegistrationControllerTestHelper {
    public static final String VALID_REQUEST =
            "{\"username\": \"User\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"}";
    public static final String INVALID_CREDENTIALS =
            "{\"username\": \"invalid\", \"email\": \"invalid\", \"password\": \"invalid\"}";
    public static final String MALFORMED_REQUEST =
            "{\"username\": \"User\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"";
    public static final String EMPTY_CREDENTIALS =
            "{\"username\": \"\", \"email\": \"\", \"password\": \"\"}";

    public static void mockSuccessRegistration(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenReturn(
                new ApiResponse("User registered successfully"));
    }
    public static void mockInvalidCredentials(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenThrow(new RegisterException(
                "Invalid input"));
    }
    public static void mockExistEmail(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Email is already exist"));
    }
    public static void mockInvalidUsernameFormat(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Invalid username format"));
    }
    public static void mockInvalidEmailFormat(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Invalid email format"));
    }
    public static void mockInvalidPasswordFormat(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Invalid password format"));
    }
}
