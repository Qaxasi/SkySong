package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserSessionChecker {

    private final DataSource dataSource;

    public UserSessionChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean userHasSession(int userId) {
        try {
            String query = "SELECT COUNT(*) FROM sessions WHERE user_id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during checking if user have session " + e.getMessage(), e);
        }
    }
}
