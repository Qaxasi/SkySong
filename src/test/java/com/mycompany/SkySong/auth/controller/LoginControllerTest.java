package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.controller.LoginRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class LoginControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        // given
        String loginJson = LoginRequests.validCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(200));
    }
    @Test
    void whenLoginSuccess_TokenCookieIsSet() throws Exception {
        // given
        String loginJson = LoginRequests.validCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(cookie().exists("auth_token"));
    }
    @Test
    void whenLoginFails_TokenCookieIsNotSet() throws Exception {
        // given
        String loginJson = LoginRequests.invalidCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(cookie().doesNotExist("auth_token"));
    }
    @Test
    void whenUserLogsIn_TokenCookieIsSetHttpOnly() throws Exception {
        // given
        String loginJson = LoginRequests.validCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(cookie().httpOnly("auth_token", true));
    }
    @Test
    void whenUserLogsIn_TokenCookieExpirationIsSetCorrectly() throws Exception {
        // given
        String loginJson = LoginRequests.validCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(cookie().maxAge("auth_token", 86400));
    }
    @Test
    void whenUserLogsIn_TokenCookieIsMarkedAsSecure() throws Exception {
        // given
        String loginJson = LoginRequests.validCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(cookie().secure("auth_token", true));
    }
    @Test
    void whenLoginSuccess_ReturnNotEmptyTokenField() throws Exception {
        // given
        String loginJson = LoginRequests.validCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
    @Test
    void whenInvalidLogin_ReturnUnauthorizedStatus() throws Exception {
        // given
        String loginJson = LoginRequests.invalidCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(401));
    }
    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        // given
        String loginJson = LoginRequests.malformedJson;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(400));
    }
    @Test
    public void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        // given
        String loginJson = LoginRequests.emptyCredentials;

        // when & then
        mockMvc.perform(post(LoginRequests.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(400));
    }
    @Test
    void whenEmptyCredentials_ReturnCorrectErrorMessage() throws Exception {
        // given
        String loginJson = LoginRequests.emptyCredentials;

        // when
        ResultActions actions = mockMvc.perform(post(LoginRequests.loginUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson));

        // then
        actions.andExpect(jsonPath("$.errors.usernameOrEmail")
                        .value("The usernameOrEmail field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }
}
