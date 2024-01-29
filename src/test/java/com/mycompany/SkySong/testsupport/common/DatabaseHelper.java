package com.mycompany.SkySong.testsupport.common;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class DatabaseHelper {
    private final DataSource dataSource;
    public DatabaseHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public boolean userExist(String username) {
        try {
            String query = "SELECT COUNT(*) " +
                    "FROM users " +
                    "WHERE username = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, username);

                ResultSet resultSet = statement.executeQuery();

                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
                throw new RuntimeException("Error during checking if user exists " + e.getMessage(), e);
        }
    }
    public boolean hasUserRole(String username, String roleName) {
        try {
            String query = "SELECT COUNT(*) FROM user_roles ur " +
                    "JOIN users u ON ur.user_id = u.id " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE u.username = ? AND r.name = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, username);
                statement.setString(2, roleName);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during checking user role: " + e.getMessage(), e);
        }
    }
}
