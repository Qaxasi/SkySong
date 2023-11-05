package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.DeleteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteServiceImplTest {
    private DeleteService deleteService;
    @Mock
    private UserDAO userDAO;
    @BeforeEach
    void init() {
        deleteService = new DeleteServiceImpl(userDAO);
    }
    @Test
    void shouldReturnSuccessMessageOnValidUserIdDeletion() {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);
        User user = new User(1, "testUsername", "testEmail@gmail.com",
                "testPassword@123", roles);

        long userId = 1L;

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        DeleteResponse response = deleteService.deleteUser(userId);

        String expectedMessage = "User with ID: " + userId + " deleted successfully.";

        assertEquals(expectedMessage, response.message());
    }
    @Test
    void shouldThrowErrorMessageWhenUserDoesNotExist() {
        long userId = 1L;
        when(userDAO.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> deleteService.deleteUser(userId));

        String expectedMessage = "User with ID: " + userId + " does not exist.";

        assertEquals(expectedMessage, exception.getMessage());
    }
}
