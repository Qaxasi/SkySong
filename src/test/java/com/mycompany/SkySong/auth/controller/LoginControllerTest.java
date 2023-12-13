package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.auth.controller.LoginControllerHelper;
import com.mycompany.SkySong.testsupport.controller.CookieAssertions;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDAO userDAO;
    private final String endpoint = "/api/v1/users/login";
    @BeforeEach
    void setUp() {
        LoginControllerHelper.setup(userDAO);
    }
    @Test
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc, endpoint, LoginControllerHelper.validCredentials, 200);
    }
    @Test
    void whenLoginSuccess_TokenCookieIsSet() throws Exception {
        CookieAssertions.assertCookieExist(
                mockMvc, endpoint, LoginControllerHelper.validCredentials);
    }
    @Test
    void whenLoginFails_TokenCookieIsNotSet() throws Exception {
        CookieAssertions.assertCookieNotExist(
                mockMvc, endpoint, LoginControllerHelper.invalidCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieIsSetHttpOnly() throws Exception {
        CookieAssertions.assertCookieIsHttpOnly(
                mockMvc, endpoint, LoginControllerHelper.validCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieExpirationIsSetCorrectly() throws Exception {
        CookieAssertions.assertCookieMaxAge(
                mockMvc, endpoint, LoginControllerHelper.validCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieIsMarkedAsSecure() throws Exception {
        CookieAssertions.assertCookieIsSecure(
                mockMvc, endpoint, LoginControllerHelper.validCredentials);
    }
    @Test
    void whenLoginSuccess_ReturnNotEmptyTokenField() throws Exception {
        PostRequestAssertions.assertPostFieldsReturns(
                mockMvc, endpoint, LoginControllerHelper.validCredentials,
                jsonPath("$.accessToken").isNotEmpty());
    }
    @Test
    void whenInvalidLogin_ReturnUnauthorizedStatus() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc, endpoint, LoginControllerHelper.invalidCredentials, 401);
    }
    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc, endpoint, LoginControllerHelper.malformedJson, 400);
    }
    @Test
    public void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc, endpoint, LoginControllerHelper.emptyCredentials, 400);
    }
    @Test
    void whenEmptyCredentials_ReturnCorrectErrorMessage() throws Exception {
        PostRequestAssertions.assertPostJsonReturns(
                mockMvc, endpoint, LoginControllerHelper.emptyCredentials,
                Map.of("$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
}
