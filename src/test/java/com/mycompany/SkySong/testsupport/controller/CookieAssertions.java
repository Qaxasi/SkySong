package com.mycompany.SkySong.testsupport.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

public class CookieAssertions {
    public static void assertCookieExist(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().exists("auth_token"));
    }
    public static void assertCookieNotExist(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().doesNotExist("auth_token"));
    }
    public static void assertCookieIsHttpOnly(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().httpOnly("auth_token", true));
    }
    public static void assertCookieMaxAge(MockMvc mockMvc, String endpoint, String requestBody,
                                          String cookieName, int expectedMaxAge) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().maxAge(cookieName, expectedMaxAge));
    }
    public static void assertCookieIsSecure(MockMvc mockMvc, String endpoint, String requestBody,
                                            String cookieName) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().secure(cookieName, true));
    }

    public static void assertCookieIsDeleted(MockMvc mockMvc, String endpoint, Cookie cookie) throws Exception {
        mockMvc.perform(post(endpoint).cookie(cookie))
                .andExpect(result -> {
                    String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
                    assertNotNull(setCookieHeader);
                    assertTrue(setCookieHeader.contains("auth_token=") &&
                            (setCookieHeader.contains("Max-Age=0") || setCookieHeader.contains("Expires")));
                });
    }
}
