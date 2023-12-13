package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.auth.controller.LoginControllerHelper;
import com.mycompany.SkySong.testsupport.controller.CookieAssertions;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

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
    void shouldRespondWithOkStatusOnSuccessfulLogin() throws Exception {
        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", LoginControllerHelper.validJson, 200);
    }
    @Test
    void shouldSetAuthTokenCookieOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String cookieName = "auth_token";

        CookieAssertions.assertCookieExist(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldNotSetAuthTokenCookieOnFailedLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"invalidPassword\"}";
        String cookieName = "auth_token";

        CookieAssertions.assertCookieDoesNotExist(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldSetAuthTokenCookieHttpOnlyOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String cookieName = "auth_token";

        CookieAssertions.assertCookieIsHttpOnly(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldSetCorrectExpirationForAuthTokenCookie() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String cookieName = "auth_token";
        int expectedMaxAge = 24 * 60 * 60;

        CookieAssertions.assertCookieMaxAge(mockMvc, "/api/v1/users/login",
                requestBody, cookieName, expectedMaxAge);
    }
    @Test
    void shouldMarkAuthTokenCookieAsSecureOnLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String cookieName = "auth_token";

        CookieAssertions.assertCookieIsSecure(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldReturnNonEmptyAccessTokenOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostFieldsReturns(mockMvc,"/api/v1/users/login",
                requestBody,
                jsonPath("$.accessToken").isNotEmpty());
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testInvalidUsername\",\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 401);
    }
    @Test
    void shouldReturnBadRequestForMalformedLoginRequestBody() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"";

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 400);
    }
}
