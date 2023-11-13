package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationFilter;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.DeleteUserService;
import com.mycompany.SkySong.testsupport.controller.DeleteRequestAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.sql.DataSource;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(DeleteUserController.class)
public class DeleteUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeleteUserService deleteUserService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnStatusOkWhenUserDeletedGivenValidUserId() throws Exception{
        long userId = 1L;

        DeleteResponse expectedResponse = new DeleteResponse("User with ID: " + userId + " deleted successfully.");

        when(deleteUserService.deleteUser(userId)).thenReturn(expectedResponse);

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 200);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnStatusNotFoundWhenUserDeletionGivenInvalidUserId() throws Exception {
        long userId = 10L;

        when(deleteUserService.deleteUser(userId)).thenThrow(
                new UserNotFoundException("User with this ID does not exist"));

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 404);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnBadRequestOnDeleteWithNoUserId() throws Exception {
        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/",  400);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnBadRequestWhenUserDeletionGivenInvalidUserIdFormat() throws Exception {
        String invalidUserId = "invalidFormat";

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + invalidUserId, 400);
    }
    @Test
    @WithMockUser(roles="USER")
    void shouldReturnStatusForbiddenWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        long userId = 1L;

        when(deleteUserService.deleteUser(userId)).thenThrow(new AccessDeniedException("Access denied."));

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 403);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnMessageOnUserDeletionWithInvalidIdFormat() throws Exception {
        String invalidUserId = "invalidFormat";
        String expectedMessage = "Invalid input data format";

        DeleteRequestAssertions.assertDeleteResponse(mockMvc, "/api/v1/users/" + invalidUserId, expectedMessage);
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReturnMessageOnUserDeletionWithNoUserId() throws Exception {
        String expectedMessage = "User ID is required and cannot be empty.";

        DeleteRequestAssertions.assertDeleteResponse(mockMvc, "/api/v1/users/", expectedMessage);
    }
    @Test
    void shouldReturnUnauthorizedWhenDeletingUserWithoutBeingAuthenticated() throws Exception {
        long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 401);
    }
}
