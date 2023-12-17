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
    public static final String EXIST_USERNAME =
            "{\"username\": \"existUser\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"}";

    public static void mockSuccessRegistration(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenReturn(
                new ApiResponse("User registered successfully"));
    }
    public static void mockExistUsername(RegistrationService registration) {
        when(registration.register(any(RegisterRequest.class))).thenThrow(new RegisterException(
                "Username is already exist"));
    }
}
