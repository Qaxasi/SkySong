package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.LoginRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class LoginControllerTest extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

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
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(LoginRequests.VALID_CREDENTIALS)))
                .andExpect(status().is(200));
    }

    @Test
    void whenLoginSuccess_MessageNotEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(LoginRequests.VALID_CREDENTIALS)))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void whenLoginSuccess_SetSessionCookie() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(LoginRequests.VALID_CREDENTIALS)))
                .andExpect(cookie().exists("session_id"));
    }

    @Test
    void whenInvalidCredentials_ReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(LoginRequests.INVALID_PASSWORD)))
                .andExpect(status().is(401));
    }

    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginRequests.MALFORMED_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void whenEmptyCredentials_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(LoginRequests.EMPTY_CREDENTIALS)))
                .andExpect(status().is(400));
    }
    @Test
    void whenEmptyCredentials_ReturnErrorMessage() throws Exception {
        ResultActions actions = mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(LoginRequests.EMPTY_CREDENTIALS)));

        actions.andExpect(jsonPath("$.errors.usernameOrEmail")
                        .value("The usernameOrEmail field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }
}
