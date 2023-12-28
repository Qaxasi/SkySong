package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieRetriever;
import com.mycompany.SkySong.auth.security.JwtAuthenticationFilter;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTestHelper {
    private static void configureRequest(MockHttpServletRequest request, String path,
                                         CookieRetriever cookieRetriever, String token) {
        setupRequestPath(request, path);

        if (token != null) {
            when(cookieRetriever.getCookie(request, "auth_token"))
                    .thenReturn(Optional.of(new Cookie("auth_token", token)));
        } else {
            when(cookieRetriever.getCookie(request, "auth_token"))
                    .thenReturn(Optional.empty());
        }
    }

    private static void setupRequestPath(HttpServletRequest request,
                                         String path) {
        when(request.getRequestURI()).thenReturn(path);
    }

    public static void simulateSuccessAuth(JwtAuthenticationFilter authFilter,
                                           MockHttpServletRequest request,
                                           MockHttpServletResponse response,
                                           FilterChain filterChain,
                                           CookieRetriever cookieRetriever,
                                           JwtTokenProvider tokenProvider,
                                           UserDetailsService userDetailsService,
                                           String token,
                                           String username,
                                           String path) throws ServletException, IOException {

        configureRequest(request, path, cookieRetriever, token);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getClaimsFromToken(token)).thenReturn(Jwts.claims().setSubject(username));
        when(userDetailsService.loadUserByUsername(username)).thenReturn(
                new User(username, "", Collections.emptyList()));

        authFilter.doFilterInternal(request, response, filterChain);

    }

    public static void simulateInvalidToken(JwtAuthenticationFilter authFilter,
                                            MockHttpServletRequest request,
                                            MockHttpServletResponse response,
                                            FilterChain filterChain,
                                            CookieRetriever cookieRetriever,
                                            JwtTokenProvider tokenProvider,
                                            String path,
                                            String token) throws ServletException, IOException {

        configureRequest(request, path, cookieRetriever, token);

        when(tokenProvider.validateToken(token)).thenReturn(false);

        authFilter.doFilterInternal(request, response, filterChain);
    }

    public static void simulateNoToken(JwtAuthenticationFilter authFilter,
                                       MockHttpServletRequest request,
                                       MockHttpServletResponse response,
                                       FilterChain filterChain,
                                       CookieRetriever cookieRetriever,
                                       String path) throws ServletException, IOException {

        configureRequest(request, path, cookieRetriever, null);

        authFilter.doFilterInternal(request, response, filterChain);
    }

    public static void simulateMissingUser(JwtAuthenticationFilter authFilter,
                                               MockHttpServletRequest request,
                                               MockHttpServletResponse response,
                                               FilterChain filterChain,
                                               CookieRetriever cookieRetriever,
                                               JwtTokenProvider tokenProvider,
                                               UserDetailsService userDetailsService,
                                               String token,
                                               String nonExistUsername,
                                               String path) {

        configureRequest(request, path, cookieRetriever, token);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getClaimsFromToken(token))
                .thenReturn(Jwts.claims().setSubject(nonExistUsername));
        when(userDetailsService.loadUserByUsername(nonExistUsername))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class,
                () -> authFilter.doFilterInternal(request, response, filterChain));
    }
    public static void executeFilterChain(JwtAuthenticationFilter authFilter,
                                          MockHttpServletRequest request,
                                          MockHttpServletResponse response,
                                          FilterChain filterChain,
                                          String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        authFilter.doFilterInternal(request, response, filterChain);
    }
}
