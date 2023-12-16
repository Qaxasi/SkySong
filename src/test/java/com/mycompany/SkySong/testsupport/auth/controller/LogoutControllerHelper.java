package com.mycompany.SkySong.testsupport.auth.controller;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LogoutControllerHelper {
    public static void assertPostStatusNoBodyWithCookie(MockMvc mockMvc, String endpoint,
                                                        int expectedStatusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(expectedStatusCode));
    }
}
