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
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AccessDeniedException exception;
    @BeforeEach
    void setUp() {
        customAccessDeniedHandler = new CustomAccessDeniedHandler();
    }
}
