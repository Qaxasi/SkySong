package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.LoginRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static com.mycompany.SkySong.testsupport.UriConstants.LOGIN_URI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class LoginControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenLoginSuccess_ResponseStatusOk() throws Exception {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(200));
    }

    @Test
    void whenLoginSuccess_ReturnNotEmptyTokenField() throws Exception {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void whenInvalidLogin_ReturnUnauthorizedStatus() throws Exception {
        // given
        LoginRequest request = LoginRequests.INVALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(401));
    }

    @Test
    void whenMalformedJson_ReturnBadRequest() throws Exception {
        // given
        String request = LoginRequests.MALFORMED_JSON;

        // when & then
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(400));
    }
    @Test
    public void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        // given
        LoginRequest request = LoginRequests.EMPTY_CREDENTIALS;

        // when & then
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(LoginRequests.EMPTY_CREDENTIALS)))
                .andExpect(status().is(400));
    }
    @Test
    void whenEmptyCredentials_ReturnCorrectErrorMessage() throws Exception {
        // given
        LoginRequest request = LoginRequests.EMPTY_CREDENTIALS;

        // when
        ResultActions actions = mockMvc.perform(post(LOGIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        // then
        actions.andExpect(jsonPath("$.errors.usernameOrEmail")
                        .value("The usernameOrEmail field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }
}
