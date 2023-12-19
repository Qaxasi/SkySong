package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTestHelper {
    public static void setup(UserDAO userDAO) {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);

        User user = new User(1, "User", "mail@mail.com",
                "$2a$10$px7fuh00336krhkSkZ9cQ.FRCVpTo5MLy90cx3rSYN5tISQxsRebq", roles);

        when(userDAO.findByUsername("User")).thenReturn(Optional.of(user));
        when(userDAO.findByEmail("mail@mail.com")).thenReturn(Optional.of(user));
    }
}
