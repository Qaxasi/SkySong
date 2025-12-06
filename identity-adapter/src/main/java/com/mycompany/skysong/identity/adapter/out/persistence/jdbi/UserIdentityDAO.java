package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIdentityDAO {
    @SqlQuery("""
            SELECT id AS userId,
            username,
            passwordHash
            FROM users
            WHERE username = :username
            """)
    @RegisterConstructorMapper(UserAuthView.class)
    Optional<UserAuthView> findAuthByUsername(@Bind String username);

    @UseRowReducer(UserAccessSnapshotReducer.class)
    @SqlQuery("""
            SELECT
            u.id            AS userId,
            u.user_tag      AS userTag,
            ur.role_code    AS roleCode
            FROM users u
            LEFT JOIN user_roles ur ON ur.user_id = u.id
            WHERE u.id = :userId
            """)
    Optional<UserAccessSnapshotView> findAccessSnapshotByUserId(@Bind int userId);

    @SqlQuery("""
            SELECT 
                EXISTS (SELECT 1 FROM users WHERE username = :username) AS usernameExists,
                EXISTS (SELECT 1 FROM users WHERE email = :email) AS emailExists
             """)
    @RegisterConstructorMapper(UniquenessStatusView.class)
    UniquenessStatusView checkUniqueness(@Bind("username") String username,
                                          @Bind("email") String email);
}
