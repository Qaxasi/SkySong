package com.mycompany.SkySong.testsupport.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.mycompany.SkySong.testsupport.auth.service.RegistrationHelper.doesRoleAddedToNewUser;
import static com.mycompany.SkySong.testsupport.auth.service.RegistrationHelper.doesUserExist;
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
}
