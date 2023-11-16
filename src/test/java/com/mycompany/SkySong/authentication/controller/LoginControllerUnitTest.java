package com.mycompany.SkySong.authentication.controller;


import com.mycompany.SkySong.authentication.service.LoginService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class LoginControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginController loginController;

}
