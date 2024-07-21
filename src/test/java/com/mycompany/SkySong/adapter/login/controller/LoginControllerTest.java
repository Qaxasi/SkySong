package com.mycompany.SkySong.adapter.login.controller;

import com.mycompany.SkySong.application.login.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.mycompany.SkySong.testsupport.common.JsonUtils.asJsonString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class LoginControllerTest extends BaseIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LoginRequests requests;
    @Autowired
    private UserFixture userFixture;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        createUserWithUsername("Alex");
        assertEndpointReturns("/api/v1/users/login", requests.login("Alex"), 200);
    }

    @Test
    void whenLoginSuccess_ReturnMessage() throws Exception {
        createUserWithUsername("Alex");
        assertEndpointReturnsMessage("/api/v1/users/login", requests.login("Alex"),
                "$.message", "Logged successfully.");
    }

    @Test
    void whenLoginSuccess_SetSessionCookie() throws Exception {
        createUserWithUsername("Alex");
        assertEndpointSetsCookie("/api/v1/users/login", requests.login("Alex"), "session_id");
    }

    @Test
    void whenLoginSuccess_SessionCookieNotEmpty() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests.validCredentials)))
                .andReturn();

        Cookie cookie = mvcResult.getResponse().getCookie("session_id");
        assertNotNull(cookie);

        String cookieValue = cookie.getValue();
        assertNotNull(cookieValue);
    }

    @Test
    void whenLoginSuccess_CookieIsSetHttpOnly() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests.validCredentials)))
                .andExpect(cookie().httpOnly("session_id", true));
    }

    @Test
    void whenLoginSuccess_CookieHasCorrectPath() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests.validCredentials)))
                .andExpect(cookie().path("session_id", "/"));
    }

    @Test
    void whenInvalidCredentials_ReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests.invalidPassword)))
                .andExpect(status().is(401));
    }

    @Test
    void whenInvalidCredentials_CookieIsNotSet() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests.invalidPassword)))
                .andExpect(cookie().doesNotExist("session_id"));
    }

    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requests.malformedJson))
                .andExpect(status().is(400));
    }

    @Test
    void whenEmptyCredentials_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests.emptyCredentials)))
                .andExpect(status().is(400));
    }
    
    @Test
    void whenEmptyCredentials_ReturnErrorMessage() throws Exception {
        ResultActions actions = mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests.emptyCredentials)));

        actions.andExpect(jsonPath("$.errors.usernameOrEmail")
                        .value("The usernameOrEmail field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }

    private void assertEndpointReturns(String endpoint, LoginRequest request, int statusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(statusCode));
    }
    private void createUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }
    private void assertEndpointReturnsMessage(String endpoint, LoginRequest request,
                                              String jsonPath, String expectedMessage) throws Exception {
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(jsonPath(jsonPath).value(expectedMessage));
    }
}
