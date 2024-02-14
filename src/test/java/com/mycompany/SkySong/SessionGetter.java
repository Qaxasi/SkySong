package com.mycompany.SkySong;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SessionGetter {

    private final DataSource dataSource;

    public SessionGetter(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
