package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.*;
import com.mycompany.SkySong.shared.config.SecurityConfig;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.auth.controller.RegistrationControllerTestHelper;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@Import(SecurityConfig.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @MockBean
    private CookieRetriever cookieRetriever;
    @MockBean
    private ApplicationMessageService messageService;
    private final String endpoint = "/api/v1/users/register";
    @Test
    void whenRegistrationSuccess_Return201() throws Exception {
        RegistrationControllerTestHelper.mockSuccessRegistration(registrationService);
        PostRequestAssertions.assertPostStatus(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.VALID_REQUEST,
                201);
    }
    @Test
    void whenRegistrationSuccess_ReturnCorrectFieldName() throws Exception {
        RegistrationControllerTestHelper.mockSuccessRegistration(registrationService);
        PostRequestAssertions.assertPostRequestFields(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.VALID_REQUEST,
                jsonPath("$.message").isNotEmpty());
    }
    @Test
    void whenUsernameExist_ReturnBadRequest() throws Exception {
        PostRequestAssertions.assertPostStatus(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.EXIST_USERNAME,
                400);
    }
    @Test
    void whenEmailExist_ReturnBadRequest() throws Exception {
        RegistrationControllerTestHelper.mockExistEmail(registrationService);
        PostRequestAssertions.assertPostStatus(
                mockMvc,
                "/api/v1/users/register",
                RegistrationControllerTestHelper.EXIST_EMAIL,
                400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidUsernameFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"test-invalid-username-format\", \"email\": \"testUniqueEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        when(registrationService.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Invalid username format"));

        PostRequestAssertions.assertPostStatus(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidEmailFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"test-invalid-email-format\", " +
                        "\"password\": \"testPassword@123\"}";

        when(registrationService.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Invalid email format"));

        PostRequestAssertions.assertPostStatus(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidPasswordFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"test-invalid-password-format\"}";

        when(registrationService.register(any(RegisterRequest.class))).thenThrow(
                new RegisterException("Invalid password format"));

       PostRequestAssertions.assertPostStatus(mockMvc, "/api/v1/users/register",
               requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestForMalformedRegisterRequestBody() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"";

        PostRequestAssertions.assertPostStatus(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnCorrectErrorMessageWhenTryRegisterWithEmptyCredentials() throws Exception {
        final var requestBody =
                "{\"username\": \"\", \"email\": \"\", " +
                        "\"password\": \"\"}";

        PostRequestAssertions.assertPostJsonResponse(mockMvc, "/api/v1/users/register", requestBody,
                Map.of("$.errors.username", "The username field cannot be empty.",
                        "$.errors.email","The email field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
}
