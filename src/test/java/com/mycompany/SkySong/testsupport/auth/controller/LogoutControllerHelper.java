package com.mycompany.SkySong.testsupport.auth.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static com.mycompany.SkySong.testsupport.UriConstants.LOGIN_URI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LogoutControllerHelper {
    private final MockMvc mockMvc;
    public LogoutControllerHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
    public Cookie loginAndGetCookie() throws Exception {
        MockHttpServletResponse response =
                mockMvc.perform(post(LOGIN_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(LoginRequests.VALID_CREDENTIALS)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();

        return response.getCookie("auth_token");
    }
}
