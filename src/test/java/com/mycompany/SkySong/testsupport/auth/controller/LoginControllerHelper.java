package com.mycompany.SkySong.testsupport.auth.controller;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

public class LoginControllerHelper {
    public static void setup(UserDAO userDAO) {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);

        when(userDAO.findByEmail("testEmail@gmail.com"))
                .thenReturn(Optional.of(new User(1, "testUsername", "testEmail@gmail.com",
                        "$2a$10$VEbWwz6NcL4y6MgKEE/sJuWiFe2EoVbru6gJ.6Miu6G16NWfqlxci", roles)));
    }
}
