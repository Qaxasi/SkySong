package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {

    private final CustomPasswordEncoder encoder;
    private String username = "Username";
    private String email = "email@mail.mail";
    private String password = "Password#3";
    private Set<Role> roles = new HashSet<>();

    public UserBuilder(CustomPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserBuilder copy() {
        UserBuilder copy = new UserBuilder(this.encoder);
        copy.username = this.username;
        copy.email = this.email;
        copy.password = this.password;
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

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(Role role) {
        this.roles.add(role);
        return this;
    }

    public User build() {
        return new User.Builder()
                .withUsername(username)
                .withEmail(email)
                .withPassword(encoder.encode(password))
                .withRoles(roles)
                .build();
    }
}
