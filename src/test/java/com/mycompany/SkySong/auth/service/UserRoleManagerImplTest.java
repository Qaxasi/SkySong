package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserRoleManagerImplTest {

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
