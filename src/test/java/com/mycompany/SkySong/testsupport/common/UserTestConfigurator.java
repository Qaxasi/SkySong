package com.mycompany.SkySong.testsupport.common;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

public class UserTestConfigurator {
    public static void setupUsers(UserDAO userDAO) {
        Role userRole = new Role(UserRole.ROLE_USER);
        Set<Role> userRoles = Set.of(userRole);

        User user = new User(1, "User", "mail@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", userRoles);

        Role adminRole = new Role(UserRole.ROLE_ADMIN);
        Set<Role> adminRoles = Set.of(userRole, adminRole);

        User admin = new User("Admin", "adminMail@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", adminRoles);

        when(userDAO.findByUsername("User")).thenReturn(Optional.of(user));
        when(userDAO.findByEmail("mail@mail.com")).thenReturn(Optional.of(user));

        when(userDAO.findByUsername("Admin")).thenReturn(Optional.of(admin));
        when(userDAO.findByUsername("adminMail@mail.com")).thenReturn(Optional.of(admin));
    }
}
