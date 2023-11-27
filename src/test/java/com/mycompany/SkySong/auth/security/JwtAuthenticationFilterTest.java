package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
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
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", "invalidToken")));
        when(jwtTokenProviderImpl.validateToken("invalidToken")).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForInvalidJwtToken() throws ServletException, IOException {
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", "invalidToken")));
        when(jwtTokenProviderImpl.validateToken("invalidToken")).thenThrow(new TokenException("Invalid token."));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestWithoutToken() throws ServletException, IOException {
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldInvokeEntryPointForRequestWithoutToken() throws ServletException, IOException {
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtAuthenticationEntryPoint).commence(eq(request), eq(response),
                any(InsufficientAuthenticationException.class));
    }
    @Test
    void shouldNotSetSecurityContextForRequestWithoutToken() throws ServletException, IOException {
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotProcessRequestForExpiredToken() throws ServletException, IOException {
        String expiredToken = "expiredToken";

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(jwtTokenProviderImpl.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        assertThrows(TokenException.class, () -> jwtAuthenticationFilter
                .doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForExpiredToken() throws ServletException, IOException {
        String expiredToken = "expiredToken";

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
    void shouldNotSetSecurityContextForRequestWhenUserNotFound() throws ServletException, IOException {
        String token = "validTokenButNoUser";
        String username = "nonExistentUser";

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
    void shouldInvokeFilterChainForLoginPaths() throws ServletException, IOException {
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
    void shouldInvokeFilterChainForRegisterPaths() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/register");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldNotProcessRequestWhenUnexpectedExceptionDuringTokenValidation() throws ServletException, IOException {
        String token = "token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProviderImpl.validateToken(token)).thenThrow(
                new RuntimeException("Unexpected error processing the request"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
}
