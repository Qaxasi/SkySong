package com.mycompany.SkySong.testsupport.auth.service;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.mycompany.SkySong.testsupport.auth.service.RegistrationHelper.doesUserExist;

public class UserAssertions {
    public static void assertUserExist(String username, DataSource dataSource) throws SQLException {
        if (!doesUserExist(username, dataSource)) {
            throw new AssertionError("User " + username + " should exist in database.");
        }
    }
}
