package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.mycompany.SkySong.testsupport.auth.service.DatabaseHelper.doesRoleAddedToNewUser;
import static com.mycompany.SkySong.testsupport.auth.service.DatabaseHelper.doesUserExist;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Component
public class UserAssertions {
    private static DataSource dataSource;
    @Autowired
    public UserAssertions(DataSource dataSource) {
        UserAssertions.dataSource = dataSource;
    }
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
}
