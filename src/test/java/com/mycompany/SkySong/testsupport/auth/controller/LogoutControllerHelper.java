package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.security.CookieDeleter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LogoutControllerHelper {
    public static void setupCookieDeletion(CookieDeleter cookieDeleter, String cookieName) {
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            Cookie modifiedCookie = new Cookie(cookieName, null);
            modifiedCookie.setMaxAge(0);
            modifiedCookie.setPath("/");
            response.addCookie(modifiedCookie);
            return null;
        }).when(cookieDeleter).deleteCookie(any(HttpServletRequest.class),
                any(HttpServletResponse.class), eq(cookieName));

    }
    public static void assertPostStatusNoBodyWithCookie(MockMvc mockMvc, String endpoint, int expectedStatusCode,
                                                        String expectedMessage) throws Exception {
        mockMvc.perform(post(endpoint)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().string(expectedMessage));
    }
    public static void assertStatus(MockMvc mockMvc, String endpoint, Cookie cookieName,
                                      int expectedStatusCode) throws Exception {
        mockMvc.perform(post(endpoint).cookie(cookieName)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("User").roles("USER")))
                .andExpect(status().is(expectedStatusCode));
    }
    public static void assertStatusWithoutCookie(MockMvc mockMvc, String endpoint,
                                                     int expectedStatusCode) throws Exception {
        mockMvc.perform(post(endpoint)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("User").roles("USER")))
                .andExpect(status().is(expectedStatusCode));
    }
}
