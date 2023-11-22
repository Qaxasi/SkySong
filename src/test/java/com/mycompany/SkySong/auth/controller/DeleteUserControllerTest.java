package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.shared.config.SecurityConfig;
import com.mycompany.SkySong.shared.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.service.CustomAccessDeniedHandler;
import com.mycompany.SkySong.auth.service.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.auth.service.JwtTokenProviderImpl;
import com.mycompany.SkySong.auth.service.CookieService;
import com.mycompany.SkySong.user.service.DeleteUserService;
import com.mycompany.SkySong.testsupport.controller.DeleteRequestAssertions;
import com.mycompany.SkySong.user.controller.DeleteUserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@WebMvcTest(DeleteUserController.class)
@Import(SecurityConfig.class)
public class DeleteUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeleteUserService deleteUserService;
    @MockBean
    private JwtTokenProviderImpl jwtTokenProviderImpl;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @MockBean
    private CookieService cookieService;
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
}
