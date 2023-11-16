package com.mycompany.SkySong.authentication.controller;


import com.mycompany.SkySong.authentication.exception.GlobalExceptionHandler;
import com.mycompany.SkySong.authentication.service.LoginService;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class LoginControllerUnitTest {
    private MockMvc mockMvc;
    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginController loginController;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
}
