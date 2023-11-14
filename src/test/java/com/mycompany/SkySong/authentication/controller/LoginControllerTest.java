package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.model.dto.LoginRequest;
import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationFilter;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.LoginService;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LoginService loginService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Test
    void shouldRespondWithOkStatusOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String fakeToken = "fake-jwt-token";

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com", "testPassword@123"));

        when(loginService.login(any(LoginRequest.class))).thenReturn(fakeToken);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com",
                        "testPassword@123", Collections.emptyList()));

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 200);
    }
    @Test
    void shouldSetAuthTokenCookieOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String fakeToken = "fake-jwt-token";
        String cookieName = "auth_token";

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com", "testPassword@123"));

        when(loginService.login(any(LoginRequest.class))).thenReturn(fakeToken);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com",
                        "testPassword@123", Collections.emptyList()));

        PostRequestAssertions.assertCookieExist(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldNotSetAuthTokenCookieOnFailedLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"invalidPassword\"}";
        String cookieName = "auth_token";

        when(loginService.login(any(LoginRequest.class))).thenThrow(
                new BadCredentialsException("Invalid credentials."));

        PostRequestAssertions.assertCookieDoesNotExist(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldSetAuthTokenCookieHttpOnlyOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String fakeToken = "fake-jwt-token";
        String cookieName = "auth_token";

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com", "testPassword@123"));

        when(loginService.login(any(LoginRequest.class))).thenReturn(fakeToken);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com",
                        "testPassword@123", Collections.emptyList()));

        PostRequestAssertions.assertCookieIsHttpOnly(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldSetCorrectExpirationForAuthTokenCookie() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String fakeToken = "fake-jwt-token";
        String cookieName = "auth_token";
        int expectedMaxAge = 24 * 60 * 60;

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com", "testPassword@123"));

        when(loginService.login(any(LoginRequest.class))).thenReturn(fakeToken);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testEmail@gmail.com",
                        "testPassword@123", Collections.emptyList()));

        PostRequestAssertions.assertCookieMaxAge(mockMvc, "/api/v1/users/login",
                requestBody, cookieName, expectedMaxAge);
    }
    @Test
    void shouldMarkAuthTokenCookieAsSecureOnLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";
        String fakeToken = "fake-jwt-token";
        String cookieName = "auth_token";

        PostRequestAssertions.assertCookieIsSecure(mockMvc, "/api/v1/users/login", requestBody, cookieName);
    }
    @Test
    void shouldHaveCorrectFieldsNamesOnSuccessfulLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostFieldsReturns(mockMvc,"/api/v1/users/login",
                requestBody,
                jsonPath("$.accessToken").isNotEmpty(),
                jsonPath("$.tokenType").isNotEmpty());
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidUsernameLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testInvalidUsername\",\"password\": \"testPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 401);
    }
    @Test
    void shouldReturnUnauthorizedStatusForInvalidPasswordLogin() throws Exception {
        final var requestBody =
                "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testWrongPassword@123\"}";

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 401);
    }
    @Test
    void shouldReturnBadRequestForEmptyCredentialsLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 400);
    }
    @Test
    void shouldReturnCorrectErrorMessageWhenLoginWithEmptyCredentials() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"\",\"password\": \"\"}";

        PostRequestAssertions.assertPostJsonReturns(mockMvc,"/api/v1/users/login", requestBody,
                Map.of("$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
                        "$.errors.password", "The password field cannot be empty"));
    }
    @Test
    void shouldReturnBadRequestForMalformedLoginRequestBody() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"";

        PostRequestAssertions.assertPostStatusReturns(
                mockMvc,"/api/v1/users/login", requestBody, 400);
    }

}
