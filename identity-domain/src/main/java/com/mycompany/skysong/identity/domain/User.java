package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class User {
    private final UserId id;
    private final Username username;
    private final Email email;
    private final String password;
    private final Set<UserRole> roles;
    private final UserTag userTag;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.roles = Collections.unmodifiableSet(EnumSet.copyOf(builder.roles));
        this.userTag = builder.userTag;
    }

    public static class Builder {
        private UserId id;
        private Username username;
        private Email email;
        private String password;
        private Set<UserRole> roles = new HashSet<>();
        private UserTag userTag;

        public Builder withId(UserId id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(Username username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withRole(UserRole role) {
            this.roles.add(role);
            return this;
        }
        public Builder withDefaultRole() {
            this.roles = Set.of(UserRole.USER);
            return this;
        }

        public Builder withRoles(Set<UserRole> roles) {
            this.roles.addAll(roles);
            return this;
        }
        public Builder withUserTag(UserTag userTag) {
            this.userTag = userTag;
            return this;
        }

        public Result<User> build() {
            return validate()
                    .map(ignored -> new User(this));
        }

        private Result<Unit> validate() {
            if (username == null) {
                return Result.failure("Username cannot be null or empty", ErrorType.VALIDATION_ERROR);
            }
            if (email == null) {
                return Result.failure("Email cannot be null or empty", ErrorType.VALIDATION_ERROR);
            }
            if (password == null || password.isBlank()) {
                return Result.failure("Password cannot be null or empty", ErrorType.VALIDATION_ERROR);
            }
            if (roles.isEmpty()) {
                return Result.failure("User must have at least one role", ErrorType.VALIDATION_ERROR);
            }
            if (userTag == null) {
                return Result.failure("user tag cannot be null", ErrorType.VALIDATION_ERROR);
            }
            return Result.success();
        }
    }

    public UserId getId() {
        return id;
    }
    public Username getUsername() {
        return username;
    }
    public Email getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Set<UserRole> getRoles() {
        return Set.copyOf(roles);
    }
    public UserTag getUserTag() {
        return userTag;
    }
}