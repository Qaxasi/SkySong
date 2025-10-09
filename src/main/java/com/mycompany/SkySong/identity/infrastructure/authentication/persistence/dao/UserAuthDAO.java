package com.mycompany.SkySong.identity.infrastructure.persistence.dao.auth;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RegisterConstructorMapper(UserAuthView.class)
public interface UserAuthDAO {
    @SqlQuery("""
            SELECT id AS userId,
            username,
            passwordHash,
            enabled,
            locked
            FROM users
            WHERE username = :username
            """)
    Optional<UserAuthView> findByUsername(@Bind String username);
}
