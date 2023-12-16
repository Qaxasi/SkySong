package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.*;
import com.mycompany.SkySong.auth.security.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.auth.controller.CookieAssertions;
import com.mycompany.SkySong.testsupport.auth.controller.LogoutControllerHelper;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private final String endpoint = "/api/v1/users/logout";
    @BeforeEach
    void setup() {
        LogoutControllerHelper.setupCookieDeletion(cookieDeleter, "auth_token");
    }
    @Test
    void whenSuccessfulLogout_ReturnStatusOk() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        LogoutControllerHelper.assertStatus(mockMvc, endpoint, mockCookie, 200);
    }
    @Test
    void whenSuccessfulLogout_DeleteAuthTokenCookie() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        CookieAssertions.assertCookieIsDeleted(mockMvc, endpoint, mockCookie);
    }
    @Test
    void whenLogoutFails_HandleException() throws Exception {
        doThrow(new RuntimeException("Internal Error")).when(cookieDeleter).deleteCookie(any(), any(), any());
        LogoutControllerHelper.assertStatusWithoutCookie(mockMvc, endpoint, 500);
    }
    @Test
    void whenLogoutWithoutCookie_ReturnStatusOk() throws Exception {
        PostRequestAssertions.assertPostStatusNoBodyWithCookie(mockMvc, endpoint,200);
    }
}
