package com.mycompany.SkySong.testsupport.auth.common;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserIdFetcher {

    private final DataSource dataSource;

    public UserIdFetcher(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer fetchByUsername(String username) {
        try {
            String query = "SELECT id FROM users WHERE username = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user ID: " + e.getMessage(), e);
        }
        return null;
    }
}
