package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.DatabaseException;
import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.DeleteUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteUserServiceImplTest {
    private DeleteUserService deleteUserService;
    @Mock
    private UserDAO userDAO;
    @BeforeEach
    void init() {
        deleteUserService = new DeleteUserServiceImpl(userDAO);
    }
    @Test
    void shouldReturnSuccessMessageOnValidUserIdDeletion() {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);
        User user = new User(1, "testUsername", "testEmail@gmail.com",
                "testPassword@123", roles);

        long userId = 1L;

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        DeleteResponse response = deleteUserService.deleteUser(userId);

        String expectedMessage = "User with ID: " + userId + " deleted successfully.";

        assertEquals(expectedMessage, response.message());
    }
    @Test
    void shouldThrowErrorMessageWhenUserDoesNotExist() {
        long userId = 1L;
        when(userDAO.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> deleteUserService.deleteUser(userId));

        String expectedMessage = "User with ID: " + userId + " does not exist.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowDatabaseExceptionWhenUnexpectedErrorOccursWhileDeletingUser() {
        long userId = 1L;
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);
        User mockUser = new User(1, "testUsername", "testEmail@gmail.com",
                "testPassword@123", roles);

        when(userDAO.findById(userId)).thenReturn(Optional.of(mockUser));
        doThrow(new DatabaseException("Simulated database error")).when(userDAO).delete(mockUser);

        assertThrows(DatabaseException.class, () -> deleteUserService.deleteUser(userId));
    }
    @Test
    void shouldThrowMessageWhenUnexpectedErrorOccursWhileDeletingUser() {
        long userId = 1L;
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);
        User mockUser = new User(1, "testUsername", "testEmail@gmail.com",
                "testPassword@123", roles);

        when(userDAO.findById(userId)).thenReturn(Optional.of(mockUser));
        doThrow(new DataAccessException("Simulated database error") {}).when(userDAO).delete(mockUser);

        Exception exception = assertThrows(DatabaseException.class, () -> deleteUserService.deleteUser(userId));

        String expectedMessage = "An unexpected error occurred while deleting user. Please try again later.";

        assertEquals(expectedMessage, exception.getMessage());
    }
}
