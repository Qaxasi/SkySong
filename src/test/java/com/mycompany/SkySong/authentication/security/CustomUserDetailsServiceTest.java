package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserDAO userDAO;

}
