package com.mycompany.SkySong.domain.shared.entity;

import com.mycompany.SkySong.domain.shared.enums.UserRole;

public class Role {

    private Integer id;
    private UserRole name;

    public Role() {}

    public Role(Integer id, UserRole name) {
        this.id = id;
        this.name = name;
    }

    public Role(UserRole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserRole getName() {
        return name;
    }

    public void setName(UserRole name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.name();
    }
}
