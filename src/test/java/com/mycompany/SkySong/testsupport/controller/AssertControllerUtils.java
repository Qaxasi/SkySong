package com.mycompany.SkySong.testsupport.controller;

import jakarta.servlet.http.Cookie;
import org.json.JSONObject;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AssertControllerUtils {

    //Cookies
    public static void assertCookieExist(MockMvc mockMvc, String endpoint, String requestBody, String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().exists(cookieName));
    }
    public static void assertCookieDoesNotExist(MockMvc mockMvc, String endpoint, String requestBody,
                                                String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(cookie().doesNotExist(cookieName));
    }
    public static void assertCookieIsHttpOnly(MockMvc mockMvc, String endpoint, String requestBody,
                                              String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(cookie().httpOnly(cookieName, true));
    }
    public static void assertCookieMaxAge(MockMvc mockMvc, String endpoint, String requestBody,
                                          String cookieName, int expectedMaxAge) throws Exception {
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(cookie().maxAge(cookieName, expectedMaxAge));
    }






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
