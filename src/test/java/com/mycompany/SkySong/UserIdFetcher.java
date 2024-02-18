package com.mycompany.SkySong;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserIdFetcher {

    private final DataSource dataSource;

    public UserIdFetcher(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
