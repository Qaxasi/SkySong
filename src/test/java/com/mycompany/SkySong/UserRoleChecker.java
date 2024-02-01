package com.mycompany.SkySong;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserRoleChecker {

    private final DataSource dataSource;

    public UserRoleChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
