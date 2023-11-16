package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteUserControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDAO userDAO;
}
