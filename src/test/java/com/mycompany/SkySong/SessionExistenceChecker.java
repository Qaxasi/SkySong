package com.mycompany.SkySong;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SessionExistenceChecker {

    private final DataSource dataSource;

    public SessionExistenceChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
