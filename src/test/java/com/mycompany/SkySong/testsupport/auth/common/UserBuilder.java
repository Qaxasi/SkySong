package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
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
    private String password = "Password#3";
    private Set<Role> roles = new HashSet<>();

    public UserBuilder(RoleDAO roleDAO, PasswordEncoder encoder) {
        this.roleDAO = roleDAO;
        this.encoder = encoder;
    }

    public UserBuilder copy() {
        UserBuilder copy = new UserBuilder(this.roleDAO, this.encoder);
        copy.id = this.id;
        copy.username = this.username;
        copy.email = this.email;
        copy.roles = new HashSet<>(this.roles);
        return copy;
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
        Role defaultRole = roleDAO.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found " + UserRole.ROLE_USER));

        if (!roles.contains(defaultRole)) {
            this.roles.add(defaultRole);
        }

        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.encoder.encode("Password#3"));
        user.setRoles(this.roles);

        return user;
    }
}
