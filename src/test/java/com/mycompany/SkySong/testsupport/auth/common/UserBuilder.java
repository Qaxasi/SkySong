package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.registration.RoleNotFoundException;
import com.mycompany.SkySong.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserBuilder {

    private final RoleDAO roleDAO;
    private final PasswordEncoder encoder;

    private Integer id = 20;
    private String username = "Username";
    private String email = "email@mail.mail";
    private Set<Role> roles = new HashSet<>();

    public UserBuilder(RoleDAO roleDAO, PasswordEncoder encoder) {
        this.roleDAO = roleDAO;
        this.encoder = encoder;
        roles.add(new Role(UserRole.ROLE_USER));
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public UserBuilder withRole(UserRole roleName) {
        Role role = roleDAO.findByName(roleName).orElseThrow(
                () -> new RoleNotFoundException("Role not found " + roleName));
        this.roles.add(role);
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode("Password#3"));
        user.setRoles(roles);

        return user;
    }
}
