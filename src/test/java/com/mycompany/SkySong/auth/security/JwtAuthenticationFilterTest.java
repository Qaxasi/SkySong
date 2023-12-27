package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static com.mycompany.SkySong.testsupport.auth.security.JwtAuthenticationFilterTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter authFilter;
    @Mock
    private CookieRetriever cookieRetriever;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private CustomUserDetailsService userDetails;
    @Mock
    private MockHttpServletResponse response;
    @Mock
    private MockHttpServletRequest request;
    @Mock
    private JwtAuthenticationEntryPoint authEntryPoint;
    @Mock
    private ApplicationMessageService message;
    @Mock
    private FilterChain filterChain;
    @Test
    void whenLoginPath_InvokeFilterChain() throws ServletException, IOException {
        assertFilterChainInvoked(request, response, authFilter, filterChain, "/api/v1/users/login");
    }
    @Test
    void whenLoginPath_NotInvokeTokenValidation() throws ServletException, IOException {
        assertNoTokenValidationOnPath(authFilter, request, response, filterChain,
                tokenProvider, "/api/v1/users/login");
    }
    @Test
    void whenRegisterPath_InvokeFilterChain() throws ServletException, IOException {
        assertFilterChainInvoked(request, response, authFilter, filterChain, "/api/v1/users/register");
    }
    @Test
    void whenRegisterPath_NotInvokeTokenValidation() throws ServletException, IOException {
        assertNoTokenValidationOnPath(authFilter, request, response, filterChain,
                tokenProvider, "/api/v1/users/register");
    }
    @Test
    void whenLogoutPath_InvokeFilterChain() throws ServletException, IOException {
        assertFilterChainInvoked(request, response, authFilter, filterChain, "/api/v1/users/logout");
    }
    @Test
    void whenLogoutPath_NotInvokeTokenValidation() throws ServletException, IOException {
        assertNoTokenValidationOnPath(authFilter, request, response, filterChain,
                tokenProvider, "/api/v1/users/logout");
    }
    @Test
    void whenInvalidToken_NotProcessRequest() throws ServletException, IOException {
        assertInvalidTokenNotProcessRequest(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, "/api/v1/users/1", "invalidToken");
    }
    @Test
    void whenInvalidToken_NoSetSecurityContext() {
        assertNoAuthForInvalidToken(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, "/api/v1/users/1", "invalidToken");
    }
    @Test
    void whenInvalidToken_InvokeEntryPoint() throws IOException, ServletException {
        assertEntryPointForInvalidToken(authFilter, request, response, filterChain, tokenProvider, cookieRetriever,
                authEntryPoint, "invalid", "/api/v1/users/1");
    }
    @Test
    void whenNoToken_NoProcessRequest() throws ServletException, IOException {
        assertNoProcessRequestForMissingToken(
                authFilter, request, response, filterChain, cookieRetriever, "/api/v1/users/1");
    }
    @Test
    void whenNoToken_InvokeEntryPoint() throws ServletException, IOException {
        assertEntryPointInvokedForNoToken(
                authFilter, request, response, filterChain, cookieRetriever, authEntryPoint, "/api/v1/users/1");
    }
    @Test
    void whenNoToken_NoSetSecurityContext() throws ServletException, IOException {
        assertNoAuthForNoToken(authFilter, request, response, filterChain, cookieRetriever, "/api/v1/users/1");
    }
    @Test
    void whenAuthSuccess_FilterChainContinues() throws ServletException, IOException {
        assertSuccessfulAuthContinuesChain(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetails, "token", "User", "/api/v1/users/1");
    }
    @Test
    void whenAuthSuccess_SetSecurityContext() throws ServletException, IOException {
        assertSecurityContextSetAfterAuth(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetails, "token", "User", "/api/v1/users/1");
    }
}
