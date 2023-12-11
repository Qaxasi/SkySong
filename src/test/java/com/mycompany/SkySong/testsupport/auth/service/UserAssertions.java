package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import org.junit.jupiter.api.function.Executable;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class UserAssertions {
    public static void assertUserExist(String username) throws SQLException {
        if (!doesUserExist(username, dataSource)) {
            throw new AssertionError("User " + username + " should exist in database.");
        }
    }
    public static void assertUserDoesNotExist(String username) throws SQLException {
        if (doesUserExist(username, dataSource)) {
            throw new AssertionError("User " + username + " should not exist in database.");
        }
    }
    public static void assertUserRole(String username, String expectedRole) throws SQLException {
        if (!doesRoleAddedToNewUser(username,expectedRole, dataSource)) {
            throw new AssertionError("Expected role " + expectedRole + " for user " + username);
        }
    }
    public static void assertValidationException(Executable executable) {
        assertThrows(CredentialValidationException.class, executable);
    }
    public static void assertErrorMessage(Executable executable, String expectedMessage) {
        Exception exception = assertThrows(Exception.class, executable);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
