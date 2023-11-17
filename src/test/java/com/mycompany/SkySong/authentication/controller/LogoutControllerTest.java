package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LogoutController.class)
@Import(SecurityConfig.class)
public class LogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
