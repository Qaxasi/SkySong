package com.mycompany.SkySong.testsupport.common;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public class UserTestConfigurator {
    private static final Role USER_ROLE = new Role(UserRole.ROLE_USER);
    private static final Role ADMIN_ROLE = new Role(UserRole.ROLE_ADMIN);
    public static User createUser(String username, String email, String password, Role... roles) {
        Set<Role> rolesSet = Stream.of(roles).collect(Collectors.toSet());
        return new User(username, email, password, rolesSet);
    }
    public static User createRegularUser() {
        return createUser("User", "user@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", USER_ROLE);
    }
    public static User createAdminUser() {
        return createUser("Admin", "admin@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", USER_ROLE, ADMIN_ROLE);
    }
    public static void setupExistingUserByUsername(UserDAO userDAO, User user) {
        when(userDAO.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }
    public static void setupExistingUserByEmail(UserDAO userDAO, User user) {
        when(userDAO.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }
    public static void setupNonExistentUserByUsername(UserDAO userDAO, String usernameOrEmail) {
        when(userDAO.findByUsername(usernameOrEmail)).thenReturn(Optional.empty());
    }
    public static void setupNonExistentUserByEmail(UserDAO userDAO, String usernameOrEmail) {
        when(userDAO.findByEmail(usernameOrEmail)).thenReturn(Optional.empty());
    }
}
