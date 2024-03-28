package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SessionChecker {

    private final DataSource dataSource;

    public SessionChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean sessionExist(String sessionId) throws SQLException {
        String query = "SELECT COUNT(*) FROM sessions WHERE session_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, sessionId);
            return checkExistence(statement);
        }
    }

    public boolean userHasActiveSession(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM sessions WHERE user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            return checkExistence(statement);
        }
    }

    private boolean checkExistence(PreparedStatement statement) throws SQLException {
        try(ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }
}
