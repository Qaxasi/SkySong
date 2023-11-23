package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.security.CookieService;
import com.mycompany.SkySong.auth.security.JwtAuthenticationFilter;
import com.mycompany.SkySong.auth.security.JwtTokenProviderImpl;
import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.auth.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private CookieService cookieService;
    @Mock
    private JwtTokenProviderImpl jwtTokenProviderImpl;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private FilterChain filterChain;
    @Test
    void shouldContinueWithFilterChainAfterSuccessfulAuthentication() throws ServletException, IOException {
        String token = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProviderImpl.validateToken(token)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("username");
        when(jwtTokenProviderImpl.getClaimsFromToken(token)).thenReturn(mockClaims);

        when(customUserDetailsService.loadUserByUsername("username")).thenReturn(mock(UserDetails.class));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
    @Test
    void shouldSetAuthenticationInSecurityContextAfterSuccessfulAuthentication() throws ServletException, IOException {
        String token = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProviderImpl.validateToken(token)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("username");
        when(jwtTokenProviderImpl.getClaimsFromToken(token)).thenReturn(mockClaims);

        when(customUserDetailsService.loadUserByUsername("username")).thenReturn(mock(UserDetails.class));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotProcessRequestForInvalidJwtToken() throws ServletException, IOException {
        String token = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProviderImpl.validateToken(token)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotProcessRequestWithoutToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotProcessRequestWithMalformedToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("MalformedToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void shouldNotProcessRequestForExpiredToken() throws ServletException, IOException {
        String token = "expiredToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProviderImpl.validateToken(token)).thenThrow(new TokenException("Token expired"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
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
