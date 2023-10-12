package com.mycompany.SkySong.authentication.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.sql.Connection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerRegisterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data_sql/role-data.sql"));
            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data_sql/user-data.sql"));
        }
    }
    @AfterEach
    void cleanup() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }

    @Test
    void shouldSuccessfullyRegisterUserWithUniqueCredentials() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithExistingUsername() throws Exception {
        final var requestBody =
                "{\"username\": \"testUsername\", \"email\": \"testUniqeEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is already exists!."));
    }
    @Test
    void shouldReturnBadRequestWhenUserTryRegisterWithExistingEmail() throws Exception {
        final var requestBody =
                "{\"username\": \"testUniqueUsername\", \"email\": \"testEmail@gmail.com\", " +
                        "\"password\": \"testPassword@123\"}";

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is already exists!."));
    }

}
