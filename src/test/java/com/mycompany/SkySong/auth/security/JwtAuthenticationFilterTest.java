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
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static com.mycompany.SkySong.testsupport.auth.security.JwtAuthenticationFilterTestHelper.assertNoTokenValidationOnPath;
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
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private JwtAuthenticationEntryPoint authEntryPoint;
    @Mock
    private ApplicationMessageService message;
    @Mock
    private FilterChain filterChain;
    @Test
    void whenLoginPath_InvokeFilterChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/login");

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void whenLoginPath_NotInvokeTokenValidation() throws ServletException, IOException {
        assertNoTokenValidationOnPath(authFilter, request, response, filterChain,
                tokenProvider, "/api/v1/users/login");
    }
    @Test
    void whenRegisterPath_InvokeFilterChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/register");

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void whenRegisterPath_NotInvokeTokenValidation() throws ServletException, IOException {
        assertNoTokenValidationOnPath(authFilter, request, response, filterChain,
                tokenProvider, "/api/v1/users/register");
    }
    @Test
    void whenLogoutPath_InvokeFilterChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/logout");

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void whenLogoutPath_NotInvokeTokenValidation() throws ServletException, IOException {
        assertNoTokenValidationOnPath(authFilter, request, response, filterChain,
                tokenProvider, "/api/v1/users/logout");
    }
    @Test
    void shouldNotProcessRequestForInvalidJwtToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", "invalidToken")));
        when(tokenProvider.validateToken("invalidToken")).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> authFilter
                .doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForInvalidJwtToken() {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", "invalidToken")));
        when(tokenProvider.validateToken("invalidToken")).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> authFilter
                .doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldInvokeEntryPointForRequestWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);

        verify(authEntryPoint).commence(eq(request), eq(response),
                any(InsufficientAuthenticationException.class));
    }
    @Test
    void shouldNotSetSecurityContextForRequestWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestForExpiredToken() throws ServletException, IOException {
        String expiredToken = "expiredToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(tokenProvider.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        assertThrows(TokenException.class, () -> authFilter
                .doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForExpiredToken() {
        String expiredToken = "expiredToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(tokenProvider.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        assertThrows(TokenException.class, () -> authFilter
                .doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldInvokeEntryPointForMalformedToken() throws ServletException, IOException {
        String malformedToken = "malformedToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", malformedToken)));
        when(tokenProvider.validateToken(malformedToken))
                .thenReturn(false);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(authEntryPoint).commence(
                eq(request), eq(response), any(InsufficientAuthenticationException.class));
    }
    @Test
    void shouldNotProcessRequestForMalformedToken() throws ServletException, IOException {
        String malformedToken = "malformedToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", malformedToken)));
        when(tokenProvider.validateToken(malformedToken))
                .thenReturn(false);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForMalformedToken() throws ServletException, IOException {
        String malformedToken = "malformedToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", malformedToken)));
        when(tokenProvider.validateToken(malformedToken))
                .thenReturn(false);

        authFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
