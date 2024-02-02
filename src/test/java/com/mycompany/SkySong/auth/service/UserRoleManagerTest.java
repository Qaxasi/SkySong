package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.exception.InternalErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class RoleManagerTest {

    private UserRoleManagerImpl userRoleManagerImpl;

    @Test
    void shouldThrowExceptionWhenRoleNotExist() {
        when(roleDAO.findByName(any())).thenReturn(Optional.empty());

        assertThrows(InternalErrorException.class, () -> userRoleManagerImpl.getRoleByName(any()));
    }
    @Test
    void shouldReturnCorrectRoleWhenRoleExist() {
        UserRole role = UserRole.ROLE_USER;
        Role expectedRole = new Role(role);

        when(roleDAO.findByName(role)).thenReturn(Optional.of(expectedRole));

        Role actualRole = userRoleManagerImpl.getRoleByName(role);

        assertEquals(actualRole, expectedRole);
    }
    @Test
    void shouldReturnErrorMessageWhenRoleNotFound() {
        UserRole role = UserRole.ROLE_USER;
        String expectedMessage = "Error during registration";

        when(messageService.getMessage("user.role.not-set")).thenReturn(expectedMessage);
        when(roleDAO.findByName(role)).thenReturn(Optional.empty());

        Exception exception =
                assertThrows(InternalErrorException.class, () -> userRoleManagerImpl.getRoleByName(role));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
