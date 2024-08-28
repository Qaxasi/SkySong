package com.mycompany.SkySong.adapter.login.controller;

import com.mycompany.SkySong.adapter.login.dto.LoginRequest;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.mycompany.SkySong.testsupport.common.JsonUtils.asJsonString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class LoginControllerTest extends BaseIT {

    @Autowired
    private MockMvc mockMvc;
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
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        createUserWithUsername("Alex");
        assertStatusCode("/api/v1/auth/login", requests.login("Alex"), 200);
    }

    @Test
    void whenLoginSuccess_ReturnMessage() throws Exception {
        createUserWithUsername("Alex");
        assertJsonMessage("/api/v1/auth/login", requests.login("Alex"),
                "$.message", "Logged successfully.");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieExist() throws Exception {
        createUserWithUsername("Alex");
        assertCookieExists("/api/v1/auth/login",
                requests.login("Alex"), "jwtToken");
    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieExist() throws Exception {
        createUserWithUsername("Alex");
        assertCookieExists("/api/v1/auth/login",
                requests.login("Alex"), "refreshToken");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieNotEmpty() throws Exception {
        createUserWithUsername("Alex");
        assertCookieNotEmpty("/api/v1/auth/login",
                requests.login("Alex"), "jwtToken");
    }

    @Test
    void whenLoginSuccess_refreshTokenCookieNotEmpty() throws Exception {
        createUserWithUsername("Alex");
        assertCookieNotEmpty("/api/v1/auth/login",
                requests.login("Alex"), "refreshToken");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieIsHttpOnly() throws Exception {
        createUserWithUsername("Alex");
        assertCookieHttpOnly("/api/v1/auth/login", requests.login("Alex"), "jwtToken");
    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieIsHttpOnly() throws Exception {
        createUserWithUsername("Alex");
        assertCookieHttpOnly("/api/v1/auth/login", requests.login("Alex"), "refreshToken");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieHasCorrectPath() throws Exception {
        createUserWithUsername("Alex");
        assertCookiePath("/api/v1/auth/login", requests.login("Alex"), "jwtToken", "/api");
    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieHasCorrectPath() throws Exception {
        createUserWithUsername("Alex");
        assertCookiePath("/api/v1/auth/login", requests.login("Alex"), "refreshToken", "/refresh-token");
    }

    @Test
    void whenLoginSuccess_JwtTokenCookieHasCorrectAge() throws Exception {
        createUserWithUsername("Alex");
        assertCookieAge("/api/v1/auth/login", requests.login("Alex"), "jwtToken", 600);

    }

    @Test
    void whenLoginSuccess_RefreshTokenCookieHasCorrectAge() throws Exception {
        createUserWithUsername("Alex");
        assertCookieAge("/api/v1/auth/login", requests.login("Alex"), "refreshToken", 86400);
    }

    @Test
    void whenInvalidCredentials_ReturnUnauthorizedStatus() throws Exception {
        assertStatusCode("/api/v1/auth/login", requests.login("Max"), 401);
    }

    @Test
    void whenInvalidCredentials_JwtTokenCookieIsNotSet() throws Exception {
        assertCookieNotSet("/api/v1/users/login", requests.nonExistingUser, "jwtToken");
    }

    @Test
    void whenInvalidCredentials_RefreshTokenCookieIsNotSet() throws Exception {
        assertCookieNotSet("/api/v1/auth/login", requests.nonExistingUser, "refreshToken");
    }
//
//    @Test
//    void whenMalformedJson_ReturnBadRequest() throws Exception {
//        assertStatusCode("/api/v1/auth/login", requests.malformedJson, 400);
//    }
//
//    @Test
//    void whenEmptyCredentials_ReturnBadRequest() throws Exception {
//        assertStatusCode("/api/v1/auth/login", requests.emptyCredentials, 400);
//    }
//
//    @Test
//    void whenEmptyCredentials_ReturnErrorMessage() throws Exception {
//        assertJsonErrorMessages("/api/v1/auth/login", requests.emptyCredentials,
//                "$.errors.usernameOrEmail", "The usernameOrEmail field cannot be empty",
//                "$.errors.password", "The password field cannot be empty");
//    }

    private void createUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }
    
    private void assertJsonErrorMessages(String endpoint, LoginRequest request,
                                         String firstJsonPath, String firstMessage,
                                         String secondJsonPath, String secondMessage) throws Exception {
        ResultActions actions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        actions.andExpect(jsonPath(firstJsonPath)
                        .value(firstMessage))
                .andExpect(jsonPath(secondJsonPath)
                        .value(secondMessage));
    }

    private void assertStatusCode(String endpoint, LoginRequest request, int statusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(statusCode));
    }
    private void assertStatusCode(String endpoint, String request, int statusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(statusCode));
    }
    private void assertJsonMessage(String endpoint, LoginRequest request,
                                   String jsonPath, String expectedMessage) throws Exception {
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(jsonPath(jsonPath).value(expectedMessage));
    }
    private void assertCookieExists(String endpoint, LoginRequest request, String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(cookie().exists(cookieName));
    }
    private void assertCookieNotEmpty(String endpoint, LoginRequest request, String cookieName) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andReturn();

        Cookie cookie = mvcResult.getResponse().getCookie(cookieName);
        assertNotNull(cookie);

        String cookieValue = cookie.getValue();
        assertNotNull(cookieValue);
    }
    private void assertCookieHttpOnly(String endpoint, LoginRequest request, String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(cookie().httpOnly(cookieName, true));
    }
    private void assertCookiePath(String endpoint, LoginRequest request, String cookieName, String path) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(cookie().path(cookieName, path));
    }
    private void assertCookieNotSet(String endpoint, LoginRequest request, String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(cookie().doesNotExist(cookieName));
    }

    private void assertCookieAge(String endpoint, LoginRequest request, String cookieName, int cookieAge) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(cookie().maxAge(cookieName, cookieAge));

    }
}
