package com.mycompany.SkySong.testsupport.auth.common;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserExistenceChecker {

    private final DataSource dataSource;

    public UserExistenceChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean userExist(int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setInt(1, userId);
             return checkExistence(statement);
        } catch (SQLException e) {
            throw new RuntimeException("Error during check user exist: " + e.getMessage(), e);
        }
    }

    public boolean userExist(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setString(1, username);
             return checkExistence(statement);
        } catch (SQLException e) {
            throw new RuntimeException("Error during check user exist: " + e.getMessage(), e);
        }
    }

    private boolean checkExistence(PreparedStatement statement) throws SQLException {
        try(ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }
}
