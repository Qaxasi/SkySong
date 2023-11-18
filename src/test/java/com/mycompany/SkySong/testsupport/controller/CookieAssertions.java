package com.mycompany.SkySong.testsupport.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CookieAssertions {

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
