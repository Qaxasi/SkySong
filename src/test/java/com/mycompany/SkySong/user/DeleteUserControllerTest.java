package com.mycompany.SkySong.user;

import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.common.AuthenticationTestHelper;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class DeleteUserControllerTest extends BaseIT {

    @Autowired
    private MockMvc mockMvc;
    private AuthenticationTestHelper auth;
    @Autowired
    private UserExistenceChecker userChecker;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        auth = new AuthenticationTestHelper(mockMvc);
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenUserIdNotExist_ReturnStatusNotFound() throws Exception {
        int userId = 1000;

        Cookie sessionId = auth.loginAdminUser();

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                        .andExpect(status().isNotFound());
    }

    @Test
    void whenNoUserId_ReturnBadRequest() throws Exception {
        Cookie sessionId = auth.loginAdminUser();

        mockMvc.perform(delete("/api/v1/users/").cookie(sessionId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User ID is required and cannot be empty."));
    }

    @Test
    void whenInvalidUserIdFormat_ReturnBadRequest() throws Exception {
        Cookie sessionId = auth.loginAdminUser();

        String userId = "invalid";

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input data format."));
    }

    @Test
    void whenUserIsAdmin_SuccessDeletionReturnStatusOk() throws Exception {
        int userId = 2;

        Cookie sessionId = auth.loginAdminUser();

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void whenUserDeleted_SecondDeletionReturnNotFound() throws Exception {
        int userId = 2;

        Cookie sessionId = auth.loginAdminUser();

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId));

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUnauthenticatedUser_ReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/users/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized access. Please log in."));
    }

    @Test
    void whenRegularUser_ReturnForbidden() throws Exception {
        int userId = 2;

        Cookie sessionId = auth.loginRegularUser();

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error")
                        .value("You do not have permission to perform this operation."));
    }

    @Test
    void whenRegularUser_UserNotDeleted() throws Exception {
        int userId = 2;

        Cookie sessionId = auth.loginRegularUser();

        mockMvc.perform(delete("/api/v1/users/" + userId).cookie(sessionId))
                .andExpect(status().isForbidden());

        assertThat(userChecker.userExist(userId)).isTrue();
    }
}
