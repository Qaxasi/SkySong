package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.config.SecurityConfig;
import com.mycompany.SkySong.authentication.secutiry.CustomAccessDeniedHandler;
import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.CookieService;
import com.mycompany.SkySong.testsupport.controller.CookieAssertions;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
@Import(SecurityConfig.class)
public class LogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CookieService cookieService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @BeforeEach
    void setUp() {
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            Cookie cookie = new Cookie("auth_token", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return null;
        }).when(cookieService).deleteCookie(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                eq("auth_token"));
    }
    @Test
    @WithMockUser
    void shouldReturnStatusOkAfterSuccessfulLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        PostRequestAssertions.assertPostStatusReturnsWithoutBody(
                mockMvc,
                "/api/v1/users/logout",
                mockCookie,
                200);
    }
    @Test
    @WithMockUser
    void shouldReturnMessageAfterSuccessfulLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        String expectedMessage = "User logged out successfully";

        PostRequestAssertions.assertMessageReturns(mockMvc,
                "/api/v1/users/logout",
                mockCookie,
                expectedMessage);
    }
    @Test
    @WithMockUser
    void shouldDeleteAuthTokenCookieOnLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        CookieAssertions.assertCookieIsDeleted(mockMvc, "/api/v1/users/logout", mockCookie);
    }
    @Test
    @WithMockUser
    void shouldReturnStatusOkWhenLogoutWithoutCookie() throws Exception {
        PostRequestAssertions.assertPostStatusReturnsWithoutBodyAndCookie(mockMvc,
                "/api/v1/users/logout",
                200);
    }
    @Test
    @WithMockUser
    void shouldHandleServiceFailureException() throws Exception {
        doThrow(new RuntimeException("Internal Error")).when(cookieService).deleteCookie(any(), any(), any());

        PostRequestAssertions.assertPostStatusReturnsWithoutBodyAndCookie(
                mockMvc,
                "/api/v1/users/logout",
                500);
    }
    @Test
    @WithMockUser
    void shouldProcessLogoutForAnonymousUser() throws Exception {
        PostRequestAssertions.assertPostStatusReturnsWithoutBodyAndCookie(
                mockMvc,
                "/api/v1/users/logout",
                200);
    }
}
