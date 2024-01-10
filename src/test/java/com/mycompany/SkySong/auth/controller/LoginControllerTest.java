package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.mycompany.SkySong.testsupport.auth.controller.CookieAssertions.*;
import static com.mycompany.SkySong.testsupport.auth.controller.LoginControllerHelper.*;
import static com.mycompany.SkySong.testsupport.controller.PostRequestAssertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@AutoConfigureMockMvc
public class LoginControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDAO userDAO;
    private final String endpoint = "/api/v1/users/login";
    @BeforeEach
    void setUp() {
        setup(userDAO);
    }
    @Test
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        assertPostStatus(mockMvc, endpoint, validCredentials, 200);
    }
    @Test
    void whenLoginSuccess_TokenCookieIsSet() throws Exception {
        assertCookieExist(mockMvc, endpoint, validCredentials);
    }
    @Test
    void whenLoginFails_TokenCookieIsNotSet() throws Exception {
        assertCookieNotExist(mockMvc, endpoint, invalidCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieIsSetHttpOnly() throws Exception {
        assertCookieIsHttpOnly(mockMvc, endpoint, validCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieExpirationIsSetCorrectly() throws Exception {
        assertCookieMaxAge(mockMvc, endpoint, validCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieIsMarkedAsSecure() throws Exception {
        assertCookieIsSecure(mockMvc, endpoint, validCredentials);
    }
    @Test
    void whenLoginSuccess_ReturnNotEmptyTokenField() throws Exception {
        assertPostRequestFields(
                mockMvc, endpoint, validCredentials,
                jsonPath("$.accessToken").isNotEmpty());
    }
    @Test
    void whenInvalidLogin_ReturnUnauthorizedStatus() throws Exception {
        assertPostStatus(mockMvc, endpoint, invalidCredentials, 401);
    }
    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        assertPostStatus(mockMvc, endpoint, malformedJson, 400);
    }
    @Test
    public void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        assertPostStatus(mockMvc, endpoint, emptyCredentials, 400);
    }
    @Test
    void whenEmptyCredentials_ReturnCorrectErrorMessage() throws Exception {
        assertPostJsonResponse(
                mockMvc, endpoint, emptyCredentials,
                Map.of("$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
}
