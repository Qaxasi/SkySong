package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

public class LoginControllerHelper {
    public static final String validCredentials =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"}";
    public static final String invalidCredentials =
            "{\"usernameOrEmail\": \"invalid\",\"password\": \"invalid\"}";
    public static void setup(UserDAO userDAO) {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);

        when(userDAO.findByEmail("mail@mail.com"))
                .thenReturn(Optional.of(new User(1, "User", "mail@mail.com",
                        "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", roles)));
    }
}
