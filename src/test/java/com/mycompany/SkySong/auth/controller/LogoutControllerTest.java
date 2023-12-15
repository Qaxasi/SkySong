package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.*;
import com.mycompany.SkySong.shared.config.SecurityConfig;
import com.mycompany.SkySong.auth.security.CustomAccessDeniedHandler;
import com.mycompany.SkySong.auth.security.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.controller.CookieAssertions;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(LogoutController.class)
public class LogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CookieDeleter cookieDeleter;
    @MockBean
    private ApplicationMessageService messageService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private CookieRetriever cookieRetriever;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Test
    @WithMockUser
    void shouldReturnStatusOkAfterSuccessfulLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        PostRequestAssertions.assertPostStatusForNoBody(
                mockMvc,
                "/api/v1/users/logout",
                mockCookie,
                200);
    }
    @Test
    @WithMockUser
    void shouldReturnMessageAfterSuccessfulLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        String expectedMessage = "{\"message\":\"User logged out successfully\"}";
        when(messageService.getMessage("logout.success")).thenReturn("User logged out successfully");

        PostRequestAssertions.assertResponseMessage(mockMvc,
                "/api/v1/users/logout",
                mockCookie,
                expectedMessage);
    }
    @Test
    @WithMockUser
    void shouldDeleteAuthTokenCookieOnLogout() throws Exception {
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            Cookie modifiedCookie = new Cookie("auth_token", null);
            modifiedCookie.setMaxAge(0);
            modifiedCookie.setPath("/");
            response.addCookie(modifiedCookie);
            return null;
        }).when(cookieDeleter).deleteCookie(any(HttpServletRequest.class),
                any(HttpServletResponse.class), eq("auth_token"));

        Cookie mockCookie = new Cookie("auth_token", "token-value");
        CookieAssertions.assertCookieIsDeleted(mockMvc, "/api/v1/users/logout", mockCookie);
    }
    @Test
    @WithMockUser
    void shouldHandleServiceFailureException() throws Exception {
        doThrow(new RuntimeException("Internal Error")).when(cookieDeleter).deleteCookie(any(), any(), any());

        PostRequestAssertions.assertPostStatusNoBodyWithCookie(
                mockMvc,
                "/api/v1/users/logout",
                500);
    }
    @Test
    @WithMockUser
    void shouldReturnErrorMessageWhenHandleServiceFailureException() throws Exception {
        doThrow(new RuntimeException("Internal Error")).when(cookieDeleter).deleteCookie(any(), any(), any());
        String expectedMessage = "{\"error\":\"Logout failed due to an internal error\"}";
        when(messageService.getMessage("logout.failure")).thenReturn("Logout failed due to an internal error");

        PostRequestAssertions.assertResponseMessageWithoutCookie(
                mockMvc,
                "/api/v1/users/logout",
                expectedMessage);
    }
    @Test
    @WithMockUser
    void shouldReturnStatusOkWhenLogoutWithoutCookie() throws Exception {
        PostRequestAssertions.assertPostStatusNoBodyWithCookie(
                mockMvc,
                "/api/v1/users/logout",
                200);
    }
}
