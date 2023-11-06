package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.secutiry.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
public class CustomAccessDeniedHandlerTest {
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private AccessDeniedException accessDeniedException;
    @BeforeEach
    void setUp() {
        customAccessDeniedHandler = new CustomAccessDeniedHandler();
    }
}
