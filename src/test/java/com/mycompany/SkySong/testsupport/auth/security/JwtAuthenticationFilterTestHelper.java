package com.mycompany.SkySong.testsupport.auth.security;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public class JwtAuthenticationFilterTestHelper {
    private static void setupRequestPath(HttpServletRequest request,
                                         String path) {
        when(request.getRequestURI()).thenReturn(path);
    }
}
