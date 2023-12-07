package com.mycompany.SkySong.testsupport;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTestHelper {
    public Long getUserIdByUsername(DataSource dataSource, String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("id");
            } else {
                return null;
            }
        }
    }
}
