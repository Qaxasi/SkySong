package com.mycompany.SkySong.testsupport.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
@Component
public class LogoutHelper {
    @Autowired
    private MockMvc mockMvc;

}
