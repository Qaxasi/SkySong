package com.mycompany.SkySong.infrastructure.persistence.mapper;

import com.mycompany.SkySong.domain.shared.entity.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        User.Builder userBuilder = new User.Builder()
                .withId(rs.getInt("id"))
                .withUsername(rs.getString("username"))
                .withEmail(rs.getString("email"))
                .withPassword(rs.getString("password"))
                .withoutRoleValidation();

        return userBuilder.build();
    }
}
