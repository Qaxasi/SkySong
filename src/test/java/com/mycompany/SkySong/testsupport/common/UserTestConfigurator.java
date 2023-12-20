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
    public static User createUser(String username, String email, String password, Role... roles) {
        Set<Role> rolesSet = Stream.of(roles).collect(Collectors.toSet());
        return new User(username, email, password, rolesSet);
    }
    public static void setupUsers(UserDAO userDAO) {
        Role userRole = new Role(UserRole.ROLE_USER);
        Role adminRole = new Role(UserRole.ROLE_ADMIN);

        User regularUser = createUser("User", "mail@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", userRole);
        setupExistingUser(userDAO, regularUser);

        User adminUser = createUser("Admin", "adminMail@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", userRole, adminRole);
        setupExistingUser(userDAO, adminUser);
    }
}
