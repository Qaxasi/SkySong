package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleChecker {

    private final DataSource dataSource;

    public RoleChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
