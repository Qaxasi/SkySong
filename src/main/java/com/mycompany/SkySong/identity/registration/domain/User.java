package com.mycompany.SkySong.identity.registration.domain;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

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

        public Builder withRoles(Set<Role> roles) {
            this.roles.addAll(roles);
            return this;
        }

        public Result<User> build() {
            return validate()
                    .map(ignored -> new User(this));
        }

        private Result<Void> validate() {
            if (username == null || username.isBlank()) {
                return Result.failure("Username cannot be null or empty", ErrorType.VALIDATION_ERROR);
            }
            if (email == null || email.isBlank()) {
                return Result.failure("Email cannot be null or empty", ErrorType.VALIDATION_ERROR);
            }
            if (password == null || password.isBlank()) {
                return Result.failure("Password cannot be null or empty", ErrorType.VALIDATION_ERROR);
            }
            if (roles.isEmpty()) {
                return Result.failure("User must have at least one role", ErrorType.VALIDATION_ERROR);
            }
            return Result.success();
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