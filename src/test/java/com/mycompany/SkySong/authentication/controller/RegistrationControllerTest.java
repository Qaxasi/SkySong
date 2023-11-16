package com.mycompany.SkySong.authentication.controller;


import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.sql.Connection;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;
    @Test
    void shouldReturn201ForValidRegistrationRequest() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";
        PostRequestAssertions.assertPostStatusReturns(mockMvc,"/api/v1/users/register",
                requestBody, 201);
    }
    @Test
    void shouldHaveCorrectFieldsNamesOnSuccessfulRegistration() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";
        PostRequestAssertions.assertPostFieldsReturns(mockMvc,"/api/v1/users/register",
                requestBody,
                jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithExistingUsername() throws Exception {
        final var requestBody =
                "{\"username\": \"testUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }
    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithExistingEmail() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidUsernameFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"test-invalid-username-format\", \"email\": \"testUniqueEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidEmailFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"test-invalid-email-format\", " +
                        "\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidPasswordFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"test-invalid-password-format\"}";

       PostRequestAssertions.assertPostStatusReturns(mockMvc, "/api/v1/users/register",
               requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestForMalformedRegisterRequestBody() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"";

        PostRequestAssertions.assertPostStatusReturns(mockMvc,"/api/v1/users/register",
                requestBody, 400);
    }

    @Test
    void shouldReturnCorrectErrorMessageWhenTryRegisterWithEmptyCredentials() throws Exception {
        final var requestBody =
                "{\"username\": \"\", \"email\": \"\", " +
                        "\"password\": \"\"}";

        PostRequestAssertions.assertPostJsonReturns(mockMvc, "/api/v1/users/register", requestBody,
                Map.of("$.errors.username", "The username field cannot be empty.",
                        "$.errors.email","The email field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
}
