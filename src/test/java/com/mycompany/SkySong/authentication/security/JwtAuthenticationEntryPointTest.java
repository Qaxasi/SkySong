package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationEntryPointTest {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException authenticationException;
    @BeforeEach
    void setUp() {
        jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
    }
    @Test
    void shouldSetResponseStatusToUnauthorizedOnAuthFailure() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    @Test
    void shouldReturnContentTypeAsJsonOnAuthFailure() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        verify(response).setContentType("application/json");
    }
}
