package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.*;
import com.mycompany.SkySong.shared.config.SecurityConfig;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@Import(SecurityConfig.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationService registration;
    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private UserDetailsService userDetails;
    @MockBean
    private JwtAuthenticationEntryPoint authEntryPoint;
    @MockBean
    private CustomAccessDeniedHandler accessDeniedHandler;
    @MockBean
    private CookieRetriever cookieRetriever;
    @MockBean
    private ApplicationMessageService messageService;
    private final String endpoint = "/api/v1/users/register";
    @Test
    void whenRegistrationSuccess_Return201() throws Exception {
        RegistrationControllerTestHelper.mockSuccessRegistration(registration);
        PostRequestAssertions.assertPostStatus(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.VALID_REQUEST,
                201);
    }
    @Test
    void whenRegistrationSuccess_ReturnCorrectFieldName() throws Exception {
        RegistrationControllerTestHelper.mockSuccessRegistration(registration);
        PostRequestAssertions.assertPostRequestFields(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.VALID_REQUEST,
                jsonPath("$.message").isNotEmpty());
    }
    @Test
    void whenUsernameExist_ReturnBadRequest() throws Exception {
        RegistrationControllerTestHelper.mockExistUsername(registration);
        PostRequestAssertions.assertPostStatus(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.EXIST_USERNAME,
                400);
    }
    @Test
    void whenMalformedRequest_ReturnBadRequest() throws Exception {
        PostRequestAssertions.assertPostStatus(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.MALFORMED_REQUEST,
                400);
    }
    @Test
    void whenEmptyCredentials_ReturnErrorMessages() throws Exception {
        PostRequestAssertions.assertPostJsonResponse(
                mockMvc,
                endpoint,
                RegistrationControllerTestHelper.EMPTY_CREDENTIALS,
                Map.of("$.errors.username", "The username field cannot be empty.",
                        "$.errors.email","The email field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
}
