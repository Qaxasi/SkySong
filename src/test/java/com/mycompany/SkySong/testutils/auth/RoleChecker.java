package com.mycompany.SkySong.testutils.auth;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleChecker {

    private final DataSource dataSource;

    public RoleChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean userHasRoles(int userId) {
        try {
            String query = "SELECT COUNT(*) FROM user_roles WHERE user_id = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during checking if user has roles: " + e.getMessage(), e);
        }
    }
}
