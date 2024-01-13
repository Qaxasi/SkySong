package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.mycompany.SkySong.testsupport.auth.controller.RegistrationHelper.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class RegistrationControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @Transactional
    void whenRegistrationSuccess_Return201() throws Exception {
        mockMvc.perform(post(registrationUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCredentials))
                .andExpect(status().is(201));
    }
    @Test
    @Transactional
    void whenRegistrationSuccess_ReturnCorrectFieldName() throws Exception {
        mockMvc.perform(post(registrationUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCredentials))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
    @Test
    void whenInvalidCredentials_ReturnBadRequest() throws Exception {
        mockMvc.perform(post(registrationUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCredentials))
                .andExpect(status().is(400));
    }
    @Test
    void whenMalformedRequest_ReturnBadRequest() throws Exception {
        mockMvc.perform(post(registrationUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedRequest))
                .andExpect(status().is(400));
    }
    @Test
    void whenEmptyCredentials_ReturnErrorMessages() throws Exception {
        ResultActions actions = mockMvc.perform(post(registrationUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyCredentials));

        actions.andExpect(jsonPath("$.errors.username")
                        .value("The username field cannot be empty"))
                .andExpect(jsonPath("$.errors.email")
                        .value("The email field cannot be empty"))
                .andExpect(jsonPath("$.errors.password")
                        .value("The password field cannot be empty"));
    }
}
