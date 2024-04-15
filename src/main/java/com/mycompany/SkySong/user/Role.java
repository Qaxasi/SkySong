package com.mycompany.SkySong.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;
    private UserRole name;

    public Role(UserRole name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name.name();
    }
}
