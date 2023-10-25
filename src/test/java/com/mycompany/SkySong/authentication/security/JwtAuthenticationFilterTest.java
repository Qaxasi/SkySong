package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    private JwtAuthenticationFilterTest jwtAuthenticationFilterTest;
    @Mock
    private JwtTokenProviderTest jwtTokenProviderTest;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private FilterChain filterChain;


}
