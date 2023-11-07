package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomUserDetailsServiceIntegrationTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserDAO userDAO;
}
