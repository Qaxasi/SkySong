package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mycompany.SkySong.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoleManagerTest extends BaseIT {

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private SqlDatabaseInitializer initializer;

    @Autowired
    private SqlDatabaseCleaner cleaner;

    @Autowired
    private RoleDAO roleDAO;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenRoleExist_SuccessfullyRetrieve() {
        Role role = roleManager.getRoleByName(UserRole.valueOf("ROLE_USER"));
        assertNotNull(role);
    }
    @Test
    void whenRoleNotExist_ThrowException() {
        cleaner.clean();

        assertException(() -> roleManager.getRoleByName(UserRole.valueOf("ROLE_USER")),
                InternalErrorException.class,
                "An error occurred while processing your request. Please try again later.");

    }

//    @Test
//    void shouldReturnErrorMessageWhenRoleNotFound() {
//        UserRole role = UserRole.ROLE_USER;
//        String expectedMessage = "Error during registration";
//
//        when(messageService.getMessage("user.role.not-set")).thenReturn(expectedMessage);
//        when(roleDAO.findByName(role)).thenReturn(Optional.empty());
//
//        Exception exception =
//                assertThrows(InternalErrorException.class, () -> userRoleManagerImpl.getRoleByName(role));
//
//        assertEquals(expectedMessage, exception.getMessage());
//    }
}
