package com.mycompany.SkySong.auth.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

public class CookieAssertions {
    private static final int maxAge = 24 * 60 * 60;
    private static final String cookieName = "auth_token";
    public static void assertCookieExist(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().exists(cookieName));
    }
    public static void assertCookieNotExist(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().doesNotExist(cookieName));
    }
    public static void assertCookieIsHttpOnly(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().httpOnly(cookieName, true));
    }
    public static void assertCookieMaxAge(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().maxAge(cookieName, maxAge));
    }
    public static void assertCookieIsSecure(MockMvc mockMvc, String endpoint, String requestBody) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(cookie().secure(cookieName, true));
    }

    public static void assertCookieIsDeleted(MockMvc mockMvc, String endpoint, Cookie cookie) throws Exception {
        mockMvc.perform(post(endpoint).cookie(cookie)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(result -> {
                    String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
                    assertNotNull(setCookieHeader);
                    assertTrue(setCookieHeader.contains("auth_token=") &&
                            (setCookieHeader.contains("Max-Age=0") || setCookieHeader.contains("Expires")));
                });
    }
}
