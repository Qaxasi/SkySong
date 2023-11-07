package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationFilter;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JwtAuthenticationFilterIntegrationTest {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserDAO userDAO;
    @BeforeEach
    void setUp() {
        String jwtSecret = "jwtToken";
        long tokenExpirationTime = 1000L;
        jwtTokenProvider = new JwtTokenProvider(jwtSecret, tokenExpirationTime);
        customUserDetailsService = new CustomUserDetailsService(userDAO);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
    }
}
