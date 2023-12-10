package com.mycompany.SkySong.testsupport.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.mycompany.SkySong.testsupport.auth.service.RegistrationHelper.doesUserExist;
@Component
public class UserAssertions {
    public static void assertUserExist(String username, DataSource dataSource) throws SQLException {
        if (!doesUserExist(username, dataSource)) {
            throw new AssertionError("User " + username + " should exist in database.");
        }
    }
    public static void assertUserDoesNotExist(String username) throws SQLException {
        if (doesUserExist(username, dataSource)) {
            throw new AssertionError("User " + username + " should not exist in database.");
        }
    }
}
