package com.mycompany.SkySong.domain.shared.entity;

import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;

import java.util.HashSet;
import java.util.Set;

public class User {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User() {}

    public User(Integer id, String username, String email, String password, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public static class Builder {
       private final RoleDAO roleDAO;
       private final PasswordEncoder passwordEncoder;

        private Integer id;
        private String username;
        private String email;
        private String password;
        private Set<Role> roles = new HashSet<>();

       public Builder(RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
            this.roleDAO = roleDAO;
            this.passwordEncoder = passwordEncoder;
        }

        public Builder withId(Integer id) {
           this.id = id;
           return this;
        }

        public Builder withUsername(String username) {
           this.username = username;
           return this;
        }

        public Builder withEmail(String email) {
           this.email = email;
           return this;
        }

        public Builder withPassword(String password) {
           this.password = password;
           return this;
        }

        public Builder withRole(UserRole roleName) {
           Role role = roleDAO.findByName(roleName).orElseThrow(
                   () -> new RoleNotFoundException("Role not found " + roleName));
           this.roles.add(role);
           return this;
       }

       public User build() {
           return new User(this);
       }
    }

    public Integer getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Set<Role> getRoles() {
        return roles;
    }
}