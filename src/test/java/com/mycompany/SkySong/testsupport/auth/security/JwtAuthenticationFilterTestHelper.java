package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieRetriever;
import com.mycompany.SkySong.auth.security.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.auth.security.JwtAuthenticationFilter;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.shared.exception.TokenException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private static void setupRequestForInvalidToken(JwtAuthenticationFilter authFilter,
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
    }
    public static void assertInvalidTokenNotProcessRequest(JwtAuthenticationFilter authFilter,
                                                           MockHttpServletRequest request,
                                                           MockHttpServletResponse response,
                                                           FilterChain filterChain,
                                                           CookieRetriever cookieRetriever,
                                                           JwtTokenProvider tokenProvider,
                                                           String path,
                                                           String token) throws ServletException, IOException {

       setupRequestForInvalidToken(authFilter, request, response, filterChain,
                cookieRetriever, tokenProvider, path, token);

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

        setupRequestForInvalidToken(authFilter, request, response, filterChain,
                cookieRetriever, tokenProvider, path, token);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    public static void assertEntryPointForInvalidToken(JwtAuthenticationFilter authFilter,
                                                       MockHttpServletRequest request,
                                                       MockHttpServletResponse response,
                                                       FilterChain filterChain,
                                                       JwtTokenProvider tokenProvider,
                                                       CookieRetriever cookieRetriever,
                                                       JwtAuthenticationEntryPoint authEntryPoint,
                                                       String token,
                                                       String path) throws IOException, ServletException {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", token)));
        when(tokenProvider.validateToken(token)).thenReturn(false);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(authEntryPoint).commence(
                eq(request), eq(response), any(InsufficientAuthenticationException.class));
    }
    private static void setupRequestForNoToken(JwtAuthenticationFilter authFilter,
                                               MockHttpServletRequest request,
                                               MockHttpServletResponse response,
                                               FilterChain filterChain,
                                               CookieRetriever cookieRetriever,
                                               String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(Optional.empty());

        authFilter.doFilterInternal(request, response, filterChain);
    }
    public static void assertNoProcessRequestForNoToken(JwtAuthenticationFilter authFilter,
                                                             MockHttpServletRequest request,
                                                             MockHttpServletResponse response,
                                                             FilterChain filterChain,
                                                             CookieRetriever cookieRetriever,
                                                             String path) throws ServletException, IOException {

        setupRequestForNoToken(authFilter, request, response, filterChain, cookieRetriever, path);

        verify(filterChain, never()).doFilter(request, response);
    }
    public static void assertEntryPointInvokedForNoToken(JwtAuthenticationFilter authFilter,
                                                         MockHttpServletRequest request,
                                                         MockHttpServletResponse response,
                                                         FilterChain filterChain,
                                                         CookieRetriever cookieRetriever,
                                                         JwtAuthenticationEntryPoint authEntryPoint,
                                                         String path) throws ServletException, IOException {

        setupRequestForNoToken(authFilter, request, response, filterChain, cookieRetriever, path);

        verify(authEntryPoint).commence(eq(request), eq(response),
                any(InsufficientAuthenticationException.class));
    }
    public static void assertNoAuthForNoToken(JwtAuthenticationFilter authFilter,
                                              MockHttpServletRequest request,
                                              MockHttpServletResponse response,
                                              FilterChain filterChain,
                                              CookieRetriever cookieRetriever,
                                              String path) throws ServletException, IOException {

        setupRequestForNoToken(authFilter, request, response, filterChain, cookieRetriever, path);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    private static void setupSuccessfulAuthentication(JwtAuthenticationFilter authFilter,
                                                      MockHttpServletRequest request,
                                                      MockHttpServletResponse response,
                                                      FilterChain filterChain,
                                                      CookieRetriever cookieRetriever,
                                                      JwtTokenProvider tokenProvider,
                                                      UserDetailsService userDetailsService,
                                                      String token,
                                                      String username,
                                                      String path) throws ServletException, IOException {
        setupRequestPath(request, path);

        when(cookieRetriever.getCookie(request, "auth_token")).thenReturn(
                Optional.of(new Cookie("auth_token", token)));
        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getClaimsFromToken(token)).thenReturn(Jwts.claims().setSubject(username));
        when(userDetailsService.loadUserByUsername(username)).thenReturn(
                new User(username, "", Collections.emptyList()));

        authFilter.doFilterInternal(request, response, filterChain);

    }
    public static void assertSuccessfulAuthContinuesChain(JwtAuthenticationFilter authFilter,
                                                          MockHttpServletRequest request,
                                                          MockHttpServletResponse response,
                                                          FilterChain filterChain,
                                                          CookieRetriever cookieRetriever,
                                                          JwtTokenProvider tokenProvider,
                                                          UserDetailsService userDetailsService,
                                                          String token,
                                                          String username,
                                                          String path) throws ServletException, IOException {

        setupSuccessfulAuthentication(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetailsService, token, username, path);

        verify(filterChain).doFilter(request, response);
    }
    public static void assertSecurityContextSetAfterAuth(JwtAuthenticationFilter authFilter,
                                                         MockHttpServletRequest request,
                                                         MockHttpServletResponse response,
                                                         FilterChain filterChain,
                                                         CookieRetriever cookieRetriever,
                                                         JwtTokenProvider tokenProvider,
                                                         UserDetailsService userDetailsService,
                                                         String token,
                                                         String username,
                                                         String path) throws ServletException, IOException {

        setupSuccessfulAuthentication(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetailsService, token, username, path);

        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authContext);
        assertEquals(username, authContext.getName());
    }
    private static void simulateMissingUserAuth(JwtAuthenticationFilter authFilter,
                                                MockHttpServletRequest request,
                                                MockHttpServletResponse response,
                                                FilterChain filterChain,
                                                CookieRetriever cookieRetriever,
                                                JwtTokenProvider tokenProvider,
                                                UserDetailsService userDetailsService,
                                                String token,
                                                String nonExistUsername,
                                                String path) {
        setupRequestPath(request, path);

        when(cookieRetriever.getCookie(request, "auth_token"))
                .thenReturn(Optional.of(new Cookie("auth_token", token)));
        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getClaimsFromToken(token))
                .thenReturn(Jwts.claims().setSubject(nonExistUsername));
        when(userDetailsService.loadUserByUsername(nonExistUsername))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class,
                () -> authFilter.doFilterInternal(request, response, filterChain));
    }
    public static void assertNoProcessingForMissingUser(JwtAuthenticationFilter authFilter,
                                                        MockHttpServletRequest request,
                                                        MockHttpServletResponse response,
                                                        FilterChain filterChain,
                                                        CookieRetriever cookieRetriever,
                                                        JwtTokenProvider tokenProvider,
                                                        UserDetailsService userDetailsService,
                                                        String token,
                                                        String nonExistUsername,
                                                        String path) throws ServletException, IOException {

        simulateMissingUserAuth(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetailsService, token, nonExistUsername, path);

        verify(filterChain, never()).doFilter(request, response);
    }
}
