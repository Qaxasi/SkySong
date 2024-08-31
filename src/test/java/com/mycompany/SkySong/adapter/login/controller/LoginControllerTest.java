package com.mycompany.SkySong.adapter.login.controller;

import com.mycompany.SkySong.adapter.exception.response.ErrorResponse;
import com.mycompany.SkySong.adapter.login.dto.LoginRequest;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    private LoginRequests requests;
    private UserFixture userFixture;

    @BeforeEach
    void setup() {
        requests = new LoginRequests();

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenLoginSuccess_ResponseStatusOk() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenLoginSuccess_ReturnMessage() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Logged successfully.");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieExist() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieExists(response, "jwtToken");
    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieExist() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieExists(response, "refreshToken");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieNotEmpty() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieNotEmpty(response, "jwtToken");
    }

    @Test
    void whenLoginSuccess_refreshTokenCookieNotEmpty() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieNotEmpty(response, "refreshToken");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieIsHttpOnly() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieHttpOnly(response, "jwtToken");
    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieIsHttpOnly() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieHttpOnly(response, "refreshToken");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieHasCorrectPath() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookiePath(response, "jwtToken", "/api");
    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieHasCorrectPath() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookiePath(response, "refreshToken", "/refresh-token");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieHasCorrectAge()  {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieAge(response, "jwtToken", 600);

    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieHasCorrectAge() {
        createUserWithUsername("Alex");
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login", requests.login("Alex"));
        assertCookieAge(response, "refreshToken", 86400);
    }

    @Test
    void whenInvalidCredentials_ReturnUnauthorizedStatus() {
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login",  requests.nonExistingUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenInvalidCredentials_JwtTokenCookieIsNotSet() {
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login",  requests.nonExistingUser);
        assertCookieNotSet(response, "jwtToken");
    }

    @Test
    void whenInvalidCredentials_RefreshTokenCookieIsNotSet() {
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login",  requests.nonExistingUser);
        assertCookieNotSet(response, "refreshToken");
    }

    @Test
    void whenMalformedJson_ReturnBadRequest() {
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login",  requests.malformedJson);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenEmptyCredentials_ReturnBadRequest()  {
        ResponseEntity<ApiResponse> response = loginRequest("/api/v1/auth/login",  requests.emptyCredentials);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenEmptyCredentials_ReturnErrorMessage() {
        ResponseEntity<ErrorResponse> response = loginRequestForErrors("/api/v1/auth/login",  requests.emptyCredentials);
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(response.getBody().getErrors().get("usernameOrEmail")).isEqualTo("The usernameOrEmail field cannot be empty");
        assertThat(response.getBody().getErrors().get("password")).isEqualTo("The password field cannot be empty");
    }

    private void createUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }

    private ResponseEntity<ErrorResponse> loginRequestForErrors(String endpoint, LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        return restTemplate.postForEntity(endpoint, request, ErrorResponse.class);
    }

    private ResponseEntity<ApiResponse> loginRequest(String endpoint, LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        return restTemplate.postForEntity(endpoint, request, ApiResponse.class);
    }

    private ResponseEntity<ApiResponse> loginRequest(String endpoint, String loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(loginRequest, headers);
        return restTemplate.postForEntity(endpoint, request, ApiResponse.class);
    }

    private void assertCookieExists(ResponseEntity<ApiResponse> response, String cookieName) {
        List<String> cookies = getCookieFromResponse(response);
        assertThat(cookies).anyMatch(cookie -> cookie.startsWith(cookieName + "="));
    }

    private void assertCookieNotEmpty(ResponseEntity<ApiResponse> response, String cookieName) {
        List<String> cookies = getCookieFromResponse(response);
        assertThat(cookies).anyMatch(cookie -> cookie.startsWith(cookieName + "=") && !cookie.equals(cookieName + "="));
    }

    private void assertCookieHttpOnly(ResponseEntity<ApiResponse> response, String cookieName) {
        List<String> cookies = getCookieFromResponse(response);
        assertThat(cookies).anyMatch(cookie -> cookie.contains(cookieName) && cookie.contains("HttpOnly"));
    }

    private void assertCookiePath(ResponseEntity<ApiResponse> response, String cookieName, String path) {
        List<String> cookies = getCookieFromResponse(response);
        assertThat(cookies).anyMatch(cookie -> cookie.contains(cookieName) && cookie.contains("Path=" + path));
    }

    private void assertCookieAge(ResponseEntity<ApiResponse> response, String cookieName, int expectedAge) {
        List<String> cookies = getCookieFromResponse(response);
        assertThat(cookies).anyMatch(cookie -> cookie.contains(cookieName) && cookie.contains("Max-Age=" + expectedAge));
    }

    private void assertCookieNotSet(ResponseEntity<ApiResponse> response, String cookieName) {
        List<String> cookies = getCookieFromResponse(response);
        assertThat(cookies).noneMatch(cookie -> cookie.startsWith(cookieName + "="));
    }

    private List<String> getCookieFromResponse(ResponseEntity<?> response) {
         List<String> cookies =  response.getHeaders().get(HttpHeaders.SET_COOKIE);
         if (cookies == null || cookies.isEmpty()) {
             return Collections.emptyList();
         }
         return cookies;
    }
}
