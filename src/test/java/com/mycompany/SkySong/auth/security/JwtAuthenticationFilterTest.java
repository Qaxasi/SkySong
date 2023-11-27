package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
        when(jwtTokenProviderImpl.validateToken("invalidToken")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

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

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextForExpiredToken() throws ServletException, IOException {
        String expiredToken = "expiredToken";

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(jwtTokenProviderImpl.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldInvokeEntryPointForExpiredToken() throws ServletException, IOException {
        String expiredToken = "expiredToken";

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", expiredToken)));
        when(jwtTokenProviderImpl.validateToken(expiredToken)).thenThrow(new TokenException("Token expired"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtAuthenticationEntryPoint).commence(
                eq(request), eq(response), any(org.springframework.security.core.AuthenticationException.class));
    }
    @Test
    void shouldNotProcessRequestWhenUserNotFound() throws ServletException, IOException {
        String token = "validTokenButNoUser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProviderImpl.validateToken(token)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("unknownUser");
        when(jwtTokenProviderImpl.getClaimsFromToken(token)).thenReturn(mockClaims);

        when(customUserDetailsService.loadUserByUsername("unknownUser")).thenThrow(
                new UsernameNotFoundException("User not found"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
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
    @Test
    void shouldNotProcessRequestWithBearerPrefixOnly() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotSetSecurityContextWithoutToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
