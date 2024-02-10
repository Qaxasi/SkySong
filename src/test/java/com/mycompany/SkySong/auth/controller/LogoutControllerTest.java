package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.SessionExistenceChecker;
import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.security.TokenHasher;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.controller.LogoutControllerHelper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class LogoutControllerTest extends BaseIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LogoutControllerHelper logoutHelper;
    @Autowired
    private SessionExistenceChecker session;
    @Autowired
    private TokenHasher tokenHasher;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenLogoutWithoutCookie_StatusUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/users/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenLogoutWithValidCookie_StatusOk() throws Exception {
        Cookie sessionCookie = logoutHelper.loginAndGetCookie();

        mockMvc.perform(post("/api/v1/users/logout").cookie(sessionCookie))
                .andExpect(status().isOk());
    }

    @Test
    void whenLogoutWithValidCookie_ReturnMessage() throws Exception {
        Cookie sessionCookie = logoutHelper.loginAndGetCookie();

        mockMvc.perform(post("/api/v1/users/logout").cookie(sessionCookie))
                .andExpect(jsonPath("$.message").value("Logged out successfully."));
    }

    @Test
    void whenLogoutSuccess_DeleteSession() throws Exception {
        Cookie sessionCookie = logoutHelper.loginAndGetCookie();

        String sessionId = tokenHasher.generateHashedToken(sessionCookie.getValue());

        mockMvc.perform(post("/api/v1/users/logout").cookie(sessionCookie))
                .andExpect(status().isOk());

        assertThat(session.sessionExist(sessionId)).isFalse();
    }
}
