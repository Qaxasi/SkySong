package com.mycompany.SkySong;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserExistenceChecker {
    private final DataSource dataSource;
    public UserExistenceChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
}
