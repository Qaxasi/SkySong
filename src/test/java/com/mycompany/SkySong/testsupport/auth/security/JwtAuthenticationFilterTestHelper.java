package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieRetriever;
import com.mycompany.SkySong.auth.security.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.auth.security.JwtAuthenticationFilter;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.shared.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTestHelper {
    private static void setupRequestPath(HttpServletRequest request,
                                         String path) {
        when(request.getRequestURI()).thenReturn(path);
    }

    public static void assertNoTokenValidationOnPath(JwtAuthenticationFilter authenticationFilter,
                                                     MockHttpServletRequest request,
                                                     MockHttpServletResponse response,
                                                     FilterChain filterChain,
                                                     JwtTokenProvider jwtTokenProvider,
                                                     String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        authenticationFilter.doFilterInternal(request, response, filterChain);
        verify(jwtTokenProvider, never()).validateToken(anyString());
    }
    public static void assertFilterChainInvoked(MockHttpServletRequest request,
                                                MockHttpServletResponse response,
                                                JwtAuthenticationFilter authFilter,
                                                FilterChain filterChain,
                                                String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        authFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }
    public static void assertInvalidTokenNotProcessRequest(JwtAuthenticationFilter authFilter,
                                                           MockHttpServletRequest request,
                                                           MockHttpServletResponse response,
                                                           FilterChain filterChain,
                                                           CookieRetriever cookieRetriever,
                                                           JwtTokenProvider tokenProvider,
                                                           String path,
                                                           String token) throws ServletException, IOException {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", token)));
        when(tokenProvider.validateToken(token)).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> authFilter
                .doFilterInternal(request, response, filterChain));
        verify(filterChain, never()).doFilter(request, response);
    }
    public static void assertNoAuthForInvalidToken(JwtAuthenticationFilter authFilter,
                                                   MockHttpServletRequest request,
                                                   MockHttpServletResponse response,
                                                   FilterChain filterChain,
                                                   CookieRetriever cookieRetriever,
                                                   JwtTokenProvider tokenProvider,
                                                   String path,
                                                   String token) {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", token)));
        when(tokenProvider.validateToken(token)).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> authFilter
                .doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    public static void assertNoProcessRequestForMissingToken(JwtAuthenticationFilter authFilter,
                                                             MockHttpServletRequest request,
                                                             MockHttpServletResponse response,
                                                             FilterChain filterChain,
                                                             CookieRetriever cookieRetriever,
                                                             String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    public static void assertEntryPointInvokedForNoToken(JwtAuthenticationFilter authFilter,
                                                         MockHttpServletRequest request,
                                                         MockHttpServletResponse response,
                                                         FilterChain filterChain,
                                                         CookieRetriever cookieRetriever,
                                                         JwtAuthenticationEntryPoint authEntryPoint,
                                                         String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);

        verify(authEntryPoint).commence(eq(request), eq(response),
                any(InsufficientAuthenticationException.class));
    }
    public static void assertNoAuthForNoToken(JwtAuthenticationFilter authFilter,
                                              MockHttpServletRequest request,
                                              MockHttpServletResponse response,
                                              FilterChain filterChain,
                                              CookieRetriever cookieRetriever,
                                              String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
