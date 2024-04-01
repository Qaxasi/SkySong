package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.security.TokenHasher;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class SessionFetcher {

    private final DataSource dataSource;
    private final TokenHasher tokenHasher;

    public SessionFetcher(DataSource dataSource, TokenHasher tokenHasher) {
        this.dataSource = dataSource;
        this.tokenHasher = tokenHasher;
    }

    private Optional<Session> getSession(String hashedSessionId) {
        try {
            String query = "SELECT * FROM sessions WHERE session_id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, hashedSessionId);
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

    public Session fetchSessionForToken(String sessionToken) {
        String hashedSessionId = tokenHasher.generateHashedToken(sessionToken);
        Optional<Session> session = getSession(hashedSessionId);
        return session.orElseThrow(() -> new NoSuchElementException("Session not found for token: " + sessionToken));
    }
}
