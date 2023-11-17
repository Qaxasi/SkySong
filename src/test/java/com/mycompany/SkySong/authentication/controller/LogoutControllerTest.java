package com.mycompany.SkySong.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LogoutController.class)
public class LogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
