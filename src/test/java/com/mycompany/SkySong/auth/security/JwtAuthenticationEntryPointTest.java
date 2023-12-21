package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static com.mycompany.SkySong.testsupport.auth.security.JwtAuthenticationEntryPointTestHelper.assertAuthenticationMessage;

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
    void whenAuthFailure_ReturnMessage() throws IOException {
        assertAuthenticationMessage(jwtAuthenticationEntryPoint, request, response, authenticationException,
        "Unauthorized access. Please log in.");
    }
}
