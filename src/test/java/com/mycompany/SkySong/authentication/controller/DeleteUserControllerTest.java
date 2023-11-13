package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.DeleteUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;

    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnStatusOkWhenUserDeletedGivenValidUserId() throws Exception{
        long userId = 1L;

        AssertControllerUtils.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 200);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnStatusNotFoundWhenUserDeletionGivenInvalidUserId() throws Exception {
        long userId = 10L;

        AssertControllerUtils.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 404);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnBadRequestOnDeleteWithNoUserId() throws Exception {
        AssertControllerUtils.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/",  400);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnBadRequestWhenUserDeletionGivenInvalidUserIdFormat() throws Exception {
        String invalidUserId = "invalidFormat";

        AssertControllerUtils.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + invalidUserId, 400);
    }
    @Test
    @WithMockUser(roles="USER")
    void shouldReturnStatusForbiddenWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        long userId = 1L;

        AssertControllerUtils.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 403);
    }
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnErrorMessageWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        long userId = 1L;

        AssertControllerUtils.assertDeleteJsonReturns(mockMvc, "/api/v1/users/" + userId,
                Map.of("$.error", "You do not have permission to perform this operation."));
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnMessageOnUserDeletionWithInvalidIdFormat() throws Exception {
        String invalidUserId = "invalidFormat";
        String expectedMessage = "Invalid input data format";

        AssertControllerUtils.assertDeleteResponse(mockMvc, "/api/v1/users/" + invalidUserId, expectedMessage);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnMessageOnUserDeletionWithNoUserId() throws Exception {
        String expectedMessage = "User ID is required and cannot be empty.";

        AssertControllerUtils.assertDeleteResponse(mockMvc, "/api/v1/users/", expectedMessage);
    }
    @Test
    void shouldReturnUnauthorizedWhenDeletingUserWithoutBeingAuthenticated() throws Exception {
        long userId = 1L;

        AssertControllerUtils.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 401);
    }
    @Test
    void shouldReturnMessageWhenDeletingUserWithoutBeingAuthenticated() throws Exception {
        long userId = 1L;

        AssertControllerUtils.assertDeleteJsonReturns(mockMvc, "/api/v1/users/" + userId,
                Map.of("$.error", "Unauthorized access. Please log in."));
    }
}
