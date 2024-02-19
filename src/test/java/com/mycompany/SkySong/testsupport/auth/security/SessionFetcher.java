package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class SessionFetcher {

    private final DataSource dataSource;

    public SessionFetcher(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Session> getSession(String sessionId) {
        try {
            String query = "SELECT * FROM sessions WHERE session_id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, sessionId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    Session session = new Session();
                    session.setSessionId(resultSet.getString("session_id"));
                    session.setUserId(resultSet.getInt("user_id"));
                    session.setCreateAt(resultSet.getTimestamp("create_at"));
                    session.setExpiresAt(resultSet.getTimestamp("expires_at"));
                    return Optional.of(session);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during getting session " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}
