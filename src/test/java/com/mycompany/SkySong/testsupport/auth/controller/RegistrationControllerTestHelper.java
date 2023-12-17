package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.dto.ApiResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegistrationControllerTestHelper {
    public static final String VALID_REQUEST =
            "{\"username\": \"User\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"}";
    public static final String EXIST_USERNAME =
            "{\"username\": \"existUser\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"}";

    public static void mockSuccessRegistration(RegistrationService registrationService) {
        when(registrationService.register(any(RegisterRequest.class))).thenReturn(
                new ApiResponse("User registered successfully"));
    }
}
