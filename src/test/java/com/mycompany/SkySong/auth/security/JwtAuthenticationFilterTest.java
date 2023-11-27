package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private CookieRetriever cookieRetriever;
    @Mock
    private JwtTokenProviderImpl jwtTokenProviderImpl;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Mock
    private ApplicationMessageService messageService;
    @Mock
    private FilterChain filterChain;
    @Test
    void shouldNotProcessRequestForInvalidJwtToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", "invalidToken")));
        when(jwtTokenProviderImpl.validateToken("invalidToken")).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForInvalidJwtToken() {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", "invalidToken")));
        when(jwtTokenProviderImpl.validateToken("invalidToken")).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldInvokeEntryPointForRequestWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtAuthenticationEntryPoint).commence(eq(request), eq(response),
                any(InsufficientAuthenticationException.class));
    }
    @Test
    void shouldNotSetSecurityContextForRequestWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestForExpiredToken() throws ServletException, IOException {
        String expiredToken = "expiredToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(jwtTokenProviderImpl.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForExpiredToken() {
        String expiredToken = "expiredToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(jwtTokenProviderImpl.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestWhenUserNotFound() throws ServletException, IOException {
        String token = "validTokenButNoUser";
        String username = "nonExistentUser";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", token)));
        when(jwtTokenProviderImpl.validateToken(token)).thenReturn(true);

        Claims claims = Jwts.claims().setSubject(username);
        when(jwtTokenProviderImpl.getClaimsFromToken(token)).thenReturn(claims);

        when(customUserDetailsService.loadUserByUsername("nonExistentUser")).thenThrow(
                new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForRequestWhenUserNotFound() {
        String token = "validTokenButNoUser";
        String username = "nonExistentUser";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", token)));
        when(jwtTokenProviderImpl.validateToken(token)).thenReturn(true);

        Claims claims = Jwts.claims().setSubject(username);
        when(jwtTokenProviderImpl.getClaimsFromToken(token)).thenReturn(claims);

        when(customUserDetailsService.loadUserByUsername("nonExistentUser")).thenThrow(
                new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldInvokeFilterChainForLoginPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/login");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldNotInvokeTokenValidationForLoginPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/login");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenProviderImpl, never()).validateToken(anyString());
    }
    @Test
    void shouldInvokeFilterChainForRegisterPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/register");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldNotInvokeTokenValidationForRegisterPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/register");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenProviderImpl, never()).validateToken(anyString());
    }
    @Test
    void shouldInvokeFilterChainForLogoutPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/logout");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldNotInvokeTokenValidationForLogoutPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/logout");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenProviderImpl, never()).validateToken(anyString());
    }
    @Test
    void shouldInvokeEntryPointForMalformedToken() throws ServletException, IOException {
        String malformedToken = "malformedToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", malformedToken)));
        when(jwtTokenProviderImpl.validateToken(malformedToken))
                .thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtAuthenticationEntryPoint).commence(
                eq(request), eq(response), any(InsufficientAuthenticationException.class));
    }
    @Test
    void shouldNotProcessRequestForMalformedToken() throws ServletException, IOException {
        String malformedToken = "malformedToken";
        when(request.getRequestURI()).thenReturn("/api/v1/users/1");
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", malformedToken)));
        when(jwtTokenProviderImpl.validateToken(malformedToken))
                .thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
}
