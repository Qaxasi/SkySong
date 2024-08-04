package com.mycompany.SkySong.domain.shared.entity;

import java.util.HashSet;
import java.util.Set;

public class User {

    private final Integer id;
    private final String username;
    private final String email;
    private final String password;
    private final Set<Role> roles;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.roles = builder.roles;
    }

    public static class Builder {

        private Integer id;
        private String username;
        private String email;
        private String password;
        private final Set<Role> roles = new HashSet<>();

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

        public Builder withRole(Role role) {
            this.roles.add(role);
            return this;
       }

       public User build() {
            validate();
            return new User(this);
       }

       private void validate() {
            if (id == null || id < 0) {
                throw new IllegalArgumentException("Id cannot be null or negative");
            }
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            if (roles.isEmpty()) {
                throw new IllegalArgumentException("User must have at least one role");
            }
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