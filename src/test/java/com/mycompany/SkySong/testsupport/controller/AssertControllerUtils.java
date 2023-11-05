package com.mycompany.SkySong.testsupport.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AssertControllerUtils {
    public static void assertPostStatusReturns(MockMvc mockMvc, String endpoint, String requestBody, int expectedStatusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is(expectedStatusCode));
    }
    public static void assertDeleteStatusReturns(MockMvc mockMvc, String endpoint, int expectedStatusCode) throws Exception {
        mockMvc.perform(delete(endpoint))
                .andExpect(status().is(expectedStatusCode));
    }

    public static void assertPostJsonReturns(MockMvc mockMvc, String endpoint, String requestBody, Map<String,
            Object> jsonPathExpectations) throws Exception {
        ResultActions actions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        for (Map.Entry<String, Object> expectation : jsonPathExpectations.entrySet()) {
            actions.andExpect(jsonPath(expectation.getKey()).value(expectation.getValue()));
        }
    }
    public static void assertDeleteJsonReturns(MockMvc mockMvc, String endpoint, Map<String,
            Object> jsonPathExpectations) throws Exception {
        ResultActions actions = mockMvc.perform(delete(endpoint)
                .contentType(MediaType.APPLICATION_JSON));

        for (Map.Entry<String, Object> expectation : jsonPathExpectations.entrySet()) {
            actions.andExpect(jsonPath(expectation.getKey()).value(expectation.getValue()));
        }
    }
    public static void assertDeleteResponse(MockMvc mockMvc, String endpoint, String expectedMessage) throws Exception {
        mockMvc.perform(delete(endpoint))
                .andExpect(content().string(expectedMessage));

    }

    public static void assertPostFieldsReturns(MockMvc mockMvc, String endpoint, String requestBody,
                                               ResultMatcher... matchers) throws Exception {
        ResultActions actions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        for (ResultMatcher matcher : matchers) {
            actions.andExpect(matcher);
        }
    }
}
