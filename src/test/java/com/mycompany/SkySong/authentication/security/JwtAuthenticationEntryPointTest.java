package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationEntryPointTest {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException authenticationException;







}
