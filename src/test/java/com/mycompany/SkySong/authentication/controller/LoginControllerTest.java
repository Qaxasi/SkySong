package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.testsupport.controller.AssertControllerUtils;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;
    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/user-data.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/role-data.sql"));
        }
    }
    @AfterEach
    void cleanup() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldRespondWithOkStatusOnSuccessfulEmailLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";

        AssertControllerUtils.assertStatusReturns(
                mockMvc,"/api/v1/auth/login", requestBody, 200);

    }
    @Test
    void shouldRespondWithOkStatusOnSuccessfulUsernameLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"}";

        AssertControllerUtils.assertStatusReturns(
                mockMvc,"/api/v1/auth/login", requestBody, 200);
    }

    @Test
    void shouldHaveCorrectFieldsNamesOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"}";

        AssertControllerUtils.assertFieldsReturns(mockMvc,"/api/v1/auth/login",
                requestBody,
                jsonPath("$.accessToken").isNotEmpty(),
                jsonPath("$.tokenType").isNotEmpty());
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidUsernameLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testInvalidUsername\",\"password\": \"testPassword@123\"}";

        AssertControllerUtils.assertStatusReturns(
                mockMvc,"/api/v1/auth/login", requestBody, 401);
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidPasswordLogin() throws Exception {
        final var requestBody =
                "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testWrongPassword@123\"}";

        AssertControllerUtils.assertStatusReturns(
                mockMvc,"/api/v1/auth/login", requestBody, 401);
    }
    @Test
    void shouldReturnBadRequestForEmptyCredentialsLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        AssertControllerUtils.assertStatusReturns(
                mockMvc,"/api/v1/auth/login", requestBody, 400);
    }
    @Test
    void shouldReturnCorrectErrorMessageWhenLoginWithEmptyCredentials() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        AssertControllerUtils.assertJsonReturns(mockMvc,"/api/v1/auth/login", requestBody,
                Map.of("$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
    @Test
    void shouldReturnBadRequestForMalformedLoginRequestBody() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"";

        AssertControllerUtils.assertStatusReturns(
                mockMvc,"/api/v1/auth/login", requestBody, 400);
    }

}
