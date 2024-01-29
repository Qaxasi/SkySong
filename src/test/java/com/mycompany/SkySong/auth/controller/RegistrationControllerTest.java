package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.RegistrationRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static com.mycompany.SkySong.testsupport.UriConstants.REGISTRATION_URI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class RegistrationControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void whenRegistrationSuccess_Return201() throws Exception {
        // given
        RegisterRequest request = RegistrationRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(REGISTRATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(201));
    }
    @Test
    @Transactional
    void whenRegistrationSuccess_ReturnCorrectFieldName() throws Exception {
        // given
        RegisterRequest request = RegistrationRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(REGISTRATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
    @Test
    void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        // given
        RegisterRequest request = RegistrationRequests.INVALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(REGISTRATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(400));
    }
    @Test
    void whenMalformedRequest_ReturnBadRequest() throws Exception {
        // given
        String request = RegistrationRequests.MALFORMED_REQUEST;

        // when & then
        mockMvc.perform(post(REGISTRATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(400));
    }
    @Test
    void whenEmptyCredentials_ReturnErrorMessages() throws Exception {
        // given
        RegisterRequest request = RegistrationRequests.EMPTY_CREDENTIALS;

        // when
        ResultActions actions = mockMvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        // then
        actions.andExpect(jsonPath("$.errors.username")
                        .value("The username field cannot be empty"))
                .andExpect(jsonPath("$.errors.email")
                        .value("The email field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }
}
