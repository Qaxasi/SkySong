package com.mycompany.SkySong.identity.registration.domain;

import java.util.Objects;

public class Role {

    private Integer id;
    private UserRole name;

    public Role() {}

    public Role(Integer id, UserRole name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

    public Role(UserRole name) {
        this(null, name);
    }
    public Integer getId() {
        return id;
    }
    public UserRole getName() {
        return name;
    }
    @Override
    public String toString() {
        return name.name();
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role r)) return false;
        return Objects.equals(id, r.id) && name == r.name;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
