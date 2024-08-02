package com.mycompany.SkySong.domain.shared.entity;

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