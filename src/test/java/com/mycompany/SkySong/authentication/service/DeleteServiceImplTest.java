package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteServiceImplTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDAO userDAO;
}
