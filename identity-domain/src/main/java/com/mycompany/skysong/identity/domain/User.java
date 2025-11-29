package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class User {
    private final UserId id;
    private final String username;
    private final String email;
    private final String password;
    private final Set<UserRole> roles;
    private final boolean enabled;
    private final boolean locked;
    private final UserTag userTag;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.roles = Collections.unmodifiableSet(EnumSet.copyOf(builder.roles));
        this.enabled = builder.enabled;
        this.locked = builder.locked;
        this.userTag = builder.userTag;
    }

    public static class Builder {
        private UserId id;
        private String username;
        private String email;
        private String password;
        private boolean enabled = true;
        private boolean locked = false;
        private final Set<UserRole> roles = new HashSet<>();
        private UserTag userTag;

        public Builder withId(UserId id) {
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

        public Builder withRole(UserRole role) {
            this.roles.add(role);
            return this;
        }

        public Builder withRoles(Set<UserRole> roles) {
            this.roles.addAll(roles);
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withLocked(boolean locked) {
            this.locked = locked;
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
            if (userTag == null) {
                return Result.failure("user tag cannot be null", ErrorType.VALIDATION_ERROR);
            }
            return Result.success();
        }
    }

    // to zmapować walidację dodać logikę domenową ?

    public UserId getId() {
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
    public Set<UserRole> getRoles() {
        return Set.copyOf(roles);
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isLocked() {
        return locked;
    }
    public UserTag getUserTag() {
        return userTag;
    }
}