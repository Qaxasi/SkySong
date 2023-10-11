package com.mycompany.SkySong.authentication.controller;

import org.h2.tools.Script;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void init() throws Exception {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data_sql/user-data.sql"));
    }
    @Test
    void shouldProvideAccessTokenAndTokenTypeOnSuccessfulEmailLogin() throws Exception {
        final var requestBody = "{\"usernameOrEmail\": \"testEmail@gmail.com\",\"password\": \"testPassword@123\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").exists());
    }
   

}
