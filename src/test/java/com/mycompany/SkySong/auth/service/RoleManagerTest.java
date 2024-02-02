package com.mycompany.SkySong.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleManagerTest {

    @Autowired
    private RoleManager roleManager;

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
