package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationEntryPointTest {
    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException authenticationException;
    @Test
    void shouldReturnMessageOnAuthFailure() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        printWriter.flush();
        String jsonResponse = stringWriter.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);

        assertEquals("Unauthorized access. Please log in.", jsonObject.getString("error"));
    }
}
