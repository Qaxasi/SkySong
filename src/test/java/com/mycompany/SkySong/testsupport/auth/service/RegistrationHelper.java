package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class RegistrationHelper {
    private static RegistrationService registrationService;
    @Autowired
    public RegistrationHelper(RegistrationService registrationService) {
        RegistrationHelper.registrationService = registrationService;
    }
    public static RegisterRequest createValidRegisterRequest() {
        return new RegisterRequest("testUsername", "testEmail@gmail.com", "testPassword@123");
    }
    public static LoginRequest userLoginRequest() {
        return new LoginRequest("testUsername", "testPassword@123");
    }
    public static void givenAndExistingUser() throws DatabaseException {
        RegisterRequest request = createValidRegisterRequest();
        registrationService.register(request);
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
    public static boolean doesRoleAddedToNewUser(String username,
                                                       String roleName, DataSource dataSource) throws SQLException {
        String query = "SELECT COUNT(*) " +
                "FROM user_roles ur " +
                "JOIN users u ON ur.user_id = u.id " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.username = ? AND r.name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, roleName);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;
        }
    }
}
