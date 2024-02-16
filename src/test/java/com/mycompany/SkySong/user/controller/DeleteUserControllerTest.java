package com.mycompany.SkySong.user.controller;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.security.*;
import com.mycompany.SkySong.shared.config.SecurityConfig;
import com.mycompany.SkySong.shared.exception.UserNotFoundException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.AuthenticationTestHelper;
import com.mycompany.SkySong.testsupport.BaseIT;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    void shouldReturnStatusNotFoundWhenUserDeletionGivenInvalidUserId() throws Exception {
        long userId = 10L;

        when(deleteUserService.deleteUser(userId)).thenThrow(
                new UserNotFoundException("User with this ID does not exist"));

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 404);
    }
//    @Test
//    void shouldReturnBadRequestOnDeleteWithNoUserId() throws Exception {
//        DeleteRequestAssertions.assertDeleteStatusReturns(
//                mockMvc, "/api/v1/users/",  400);
//    }
//    @Test
//    void shouldReturnBadRequestWhenUserDeletionGivenInvalidUserIdFormat() throws Exception {
//        String invalidUserId = "invalidFormat";
//
//        DeleteRequestAssertions.assertDeleteStatusReturns(
//                mockMvc, "/api/v1/users/" + invalidUserId, 400);
//    }
//    @Test
//    void shouldReturnMessageOnUserDeletionWithInvalidIdFormat() throws Exception {
//        String invalidUserId = "invalidFormat";
//        String expectedMessage = "Invalid input data format";
//
//        DeleteRequestAssertions.assertDeleteResponse(mockMvc, "/api/v1/users/" + invalidUserId, expectedMessage);
//    }
//    @Test
//    void shouldReturnMessageOnUserDeletionWithNoUserId() throws Exception {
//        String expectedMessage = "User ID is required and cannot be empty.";
//
//        DeleteRequestAssertions.assertDeleteResponse(mockMvc, "/api/v1/users/", expectedMessage);
//    }
}
