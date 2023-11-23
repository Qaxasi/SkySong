package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.controller.LoginController;
import com.mycompany.SkySong.shared.exception.GlobalExceptionHandler;
import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class LoginControllerUnitTest {
    private MockMvc mockMvc;
    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginController loginController;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    @Test
    public void shouldReturnBadRequestForInvalidCredentials() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        PostRequestAssertions.assertPostStatusReturns(mockMvc, "/api/v1/users/login",
                requestBody, 400);
    }
    @Test
    void shouldReturnCorrectErrorMessageWhenLoginWithEmptyCredentials() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        PostRequestAssertions.assertPostJsonReturns(mockMvc,"/api/v1/users/login", requestBody,
                Map.of("$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
}
