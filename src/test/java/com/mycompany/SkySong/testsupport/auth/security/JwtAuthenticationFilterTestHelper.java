package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.JwtAuthenticationFilter;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTestHelper {
    private static void setupRequestPath(HttpServletRequest request,
                                         String path) {
        when(request.getRequestURI()).thenReturn(path);
    }

    public static void assertNoTokenValidationOnPath(JwtAuthenticationFilter authenticationFilter,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     FilterChain filterChain,
                                                     JwtTokenProvider jwtTokenProvider,
                                                     String path) throws ServletException, IOException {
        setupRequestPath(request, path);
        authenticationFilter.doFilterInternal(request, response, filterChain);
        verify(jwtTokenProvider, never()).validateToken(anyString());
    }
}
