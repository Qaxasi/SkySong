package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.controller.LoginHelper;
import com.mycompany.SkySong.testsupport.common.DatabaseHelper;
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
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.validCredentials))
                .andExpect(status().is(200));
    }
    @Test
    void whenLoginSuccess_TokenCookieIsSet() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.validCredentials))
                .andExpect(cookie().exists("auth_token"));
    }
    @Test
    void whenLoginFails_TokenCookieIsNotSet() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.invalidCredentials))
                .andExpect(cookie().doesNotExist("auth_token"));
    }
    @Test
    void whenUserLogsIn_TokenCookieIsSetHttpOnly() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.validCredentials))
                .andExpect(cookie().httpOnly("auth_token", true));
    }
    @Test
    void whenUserLogsIn_TokenCookieExpirationIsSetCorrectly() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.validCredentials))
                .andExpect(cookie().maxAge("auth_token", 86400));
    }
    @Test
    void whenUserLogsIn_TokenCookieIsMarkedAsSecure() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.validCredentials))
                .andExpect(cookie().secure("auth_token", true));
    }
    @Test
    void whenLoginSuccess_ReturnNotEmptyTokenField() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.validCredentials))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
    @Test
    void whenInvalidLogin_ReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.invalidCredentials))
                .andExpect(status().is(401));
    }
    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.malformedJson))
                .andExpect(status().is(400));
    }
    @Test
    public void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        mockMvc.perform(post(LoginHelper.loginUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LoginHelper.emptyCredentials))
                .andExpect(status().is(400));
    }
    @Test
    void whenEmptyCredentials_ReturnCorrectErrorMessage() throws Exception {
        ResultActions actions = mockMvc.perform(post(LoginHelper.loginUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(LoginHelper.emptyCredentials));

        actions.andExpect(jsonPath("$.errors.usernameOrEmail")
                        .value("The usernameOrEmail field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }
}
