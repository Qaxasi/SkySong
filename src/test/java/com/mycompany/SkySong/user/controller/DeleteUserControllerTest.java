package com.mycompany.SkySong.user.controller;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.AuthenticationTestHelper;
import com.mycompany.SkySong.testsupport.BaseIT;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class DeleteUserControllerTest extends BaseIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationTestHelper authHelper;

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
    void whenUserIdNotExist_ReturnStatusNotFound() throws Exception {
        long userId = 10L;

        Cookie sessionId = authHelper.loginAdminUser();

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                        .andExpect(status().isNotFound());
    }

    @Test
    void whenNoUserId_ReturnBadRequest() throws Exception {
        Cookie sessionId = authHelper.loginAdminUser();

        mockMvc.perform(delete("/api/v1/users/").cookie(sessionId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User ID is required and cannot be empty."));
    }

    @Test
    void whenInvalidUserIdFormat_ReturnBadRequest() throws Exception {
        Cookie sessionId = authHelper.loginAdminUser();

        String userId = "invalid";

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input data format."));
    }
}
