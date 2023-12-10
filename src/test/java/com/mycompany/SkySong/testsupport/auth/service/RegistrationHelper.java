package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationHelper {
    public static RegisterRequest newUserRegisterRequest() {
        String username = "testUniqueUsername";
        String email = "testUniqueEmail@gmail.com";
        String password = "testPassword@123";
        return new RegisterRequest(username, email, password);
    }
    public static boolean doesUserExist(String username, DataSource dataSource) throws SQLException {
        String query = "SELECT COUNT(*) " +
                       "FROM users " +
                       "WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }
}
