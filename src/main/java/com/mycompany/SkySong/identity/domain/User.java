package com.mycompany.SkySong.identity.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private final Integer id;
    private final String username;
    private final String email;
    private final String password;
    private final Set<Role> roles;

    public User(String username, String email, String password, Set<Role> roles) {
        this.id = null;
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.roles = new HashSet<>(Objects.requireNonNull(roles, "Roles cannot be null"));

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
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
        return Set.copyOf(roles);
    }
}