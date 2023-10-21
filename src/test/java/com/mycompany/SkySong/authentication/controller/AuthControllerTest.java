package com.mycompany.SkySong.authentication.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.sql.DataSource;

import java.sql.Connection;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/role-data.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/user-data.sql"));
        }
    }
    @AfterEach
    void cleanup() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }

    private void assertStatusReturns(String endpoint, String requestBody, int expectedStatusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(expectedStatusCode));
    }

    private void assertJsonReturns(String endpoint, String requestBody, Map<String,
            Object> jsonPathExpectations) throws Exception {
        ResultActions actions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        for (Map.Entry<String, Object> expectation : jsonPathExpectations.entrySet()) {
            actions.andExpect(jsonPath(expectation.getKey()).value(expectation.getValue()));
        }
    }

    private void assertFieldsReturns(String endpoint, String requestBody,
                                     ResultMatcher... matchers) throws Exception {
        ResultActions actions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        for (ResultMatcher matcher : matchers) {
            actions.andExpect(matcher);
        }
    }

    // Login-endpoint tests
    @Test
    void shouldRespondWithOkStatusOnSuccessfulEmailLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/login", requestBody, 200);

    }
    @Test
    void shouldRespondWithOkStatusOnSuccessfulUsernameLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/login", requestBody, 200);
    }

    @Test
    void shouldHaveCorrectFieldsNamesOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"}";

        assertFieldsReturns("/api/v1/auth/login",
                requestBody,
                jsonPath("$.accessToken").isNotEmpty(),
                jsonPath("$.tokenType").isNotEmpty());
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidUsernameLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testInvalidUsername\",\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/login", requestBody, 401);
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidPasswordLogin() throws Exception {
        final var requestBody =
                "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testWrongPassword@123\"}";

        assertStatusReturns("/api/v1/auth/login", requestBody, 401);
    }
    @Test
    void shouldReturnBadRequestForEmptyCredentialsLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        assertStatusReturns("/api/v1/auth/login", requestBody, 400);
    }
    @Test
    void shouldReturnCorrectErrorMessageWhenLoginWithEmptyCredentials() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        assertJsonReturns("/api/v1/auth/login", requestBody,
                Map.of("$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
    @Test
    void shouldReturnBadRequestForMalformedLoginRequestBody() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"";

        assertStatusReturns("/api/v1/auth/login", requestBody, 400);
    }


    //Register-endpoint test
    @Test
    void shouldReturn201ForValidRegistrationRequest() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";
        assertStatusReturns("/api/v1/auth/register", requestBody, 201);
    }
    @Test
    void shouldHaveCorrectFieldsNamesOnSuccessfulRegistration() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";
        assertFieldsReturns("/api/v1/auth/register",
                requestBody,
                jsonPath("$.success").isNotEmpty(),
                jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithExistingUsername() throws Exception {
        final var requestBody =
                "{\"username\": \"testUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/register", requestBody, 400);
    }
    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithExistingEmail() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/register", requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidUsernameFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"test-invalid-username-format\", \"email\": \"testUniqueEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/register", requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidEmailFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"test-invalid-email-format\", " +
                        "\"password\": \"testPassword@123\"}";

        assertStatusReturns("/api/v1/auth/register", requestBody, 400);
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithInvalidPasswordFormat() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"test-invalid-password-format\"}";

       assertStatusReturns("/api/v1/auth/register", requestBody, 400);
    }
}
