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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDAO userDAO;
    @BeforeEach
    void setUp() {
        LoginControllerHelper.setup(userDAO);
    }
    @Test
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", LoginControllerHelper.validCredentials, 200);
    }
    @Test
    void whenLoginSuccess_TokenCookieIsSet() throws Exception {
        CookieAssertions.assertCookieExist(
                mockMvc, "/api/v1/users/login", LoginControllerHelper.validCredentials);
    }
    @Test
    void whenLoginFails_TokenCookieIsNotSet() throws Exception {
        CookieAssertions.assertCookieNotExist(
                mockMvc, "/api/v1/users/login", LoginControllerHelper.invalidCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieIsSetHttpOnly() throws Exception {
        CookieAssertions.assertCookieIsHttpOnly(
                mockMvc, "/api/v1/users/login", LoginControllerHelper.validCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieExpirationIsSetCorrectly() throws Exception {
        CookieAssertions.assertCookieMaxAge(
                mockMvc, "/api/v1/users/login", LoginControllerHelper.validCredentials);
    }
    @Test
    void whenUserLogsIn_TokenCookieIsMarkedAsSecure() throws Exception {
        CookieAssertions.assertCookieIsSecure(
                mockMvc, "/api/v1/users/login", LoginControllerHelper.validCredentials);
    }
    @Test
    void whenLoginSuccess_ReturnNotEmptyTokenField() throws Exception {
        PostRequestAssertions.assertPostFieldsReturns(
                mockMvc,"/api/v1/users/login", LoginControllerHelper.validCredentials,
                jsonPath("$.accessToken").isNotEmpty());
    }
    @Test
    void whenInvalidLogin_ReturnUnauthorizedStatus() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", LoginControllerHelper.invalidCredentials, 401);
    }
    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", LoginControllerHelper.malformedJson, 400);
    }
}
