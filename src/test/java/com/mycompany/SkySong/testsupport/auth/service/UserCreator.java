package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserCreator {

    private final RoleDAO roleDAO;

    public UserCreator(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public User createUser(String username) {
        Role role = roleDAO.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new AssertionError("Role not found"));

        Set<Role> roles = Set.of(role);
        return new User( username, "new@mail.mail", "Password#3", roles);
    }
}
