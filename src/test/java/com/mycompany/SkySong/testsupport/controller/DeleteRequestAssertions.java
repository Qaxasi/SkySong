package com.mycompany.SkySong.testsupport.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DeleteRequestAssertions {
    public static void assertDeleteStatusReturns(MockMvc mockMvc, String endpoint, Cookie cookie,
                                                 int expectedStatusCode) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(endpoint);

        if (cookie != null) {
            requestBuilder.cookie(cookie);
        }
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatusCode));
    }
    public static void assertDeleteJsonReturns(MockMvc mockMvc, String endpoint, Map<String,
            Object> jsonPathExpectations) throws Exception {
        ResultActions actions = mockMvc.perform(delete(endpoint)
                .contentType(MediaType.APPLICATION_JSON));

        for (Map.Entry<String, Object> expectation : jsonPathExpectations.entrySet()) {
            actions.andExpect(jsonPath(expectation.getKey()).value(expectation.getValue()));
        }
    }
    public static void assertDeleteResponse(MockMvc mockMvc, String endpoint, Cookie jwtCookie, int expectedStatus,
                                            String expectedMessage) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(endpoint);
        if (jwtCookie != null) {
            requestBuilder.cookie(jwtCookie);
        }
        mockMvc.perform(requestBuilder)
                .andExpect(content().string(containsString(expectedMessage)));
    }
}
