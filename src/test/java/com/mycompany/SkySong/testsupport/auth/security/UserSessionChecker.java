package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserSessionChecker {

    private final DataSource dataSource;

    public UserSessionChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
