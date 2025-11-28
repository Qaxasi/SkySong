package com.mycompany.SkySong.identity.a.adapter.out.persistence.jdbi;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RegisterConstructorMapper(UserAuthView.class)
public interface UserIdentityDAO {
    @SqlQuery("""
            SELECT id AS userId,
            username,
            passwordHash,
            enabled,
            locked
            FROM users
            WHERE username = :username
            """)
    Optional<UserAuthView> findAuthCredentialsByUsername(@Bind String username);

    @UseRowReducer(UserAccessSnapshotReducer.class)
    @SqlQuery("""
            SELECT
            u.id            AS userId,
            u.user_tag      AS userTag,
            u.access_version AS accessVersion,
            ur.role_code    AS roleCode
            FROM users u
            LEFT JOIN user_roles ur ON ur.user_id = u.id
            WHERE u.id = :userId
            """)
    Optional<UserAccessSnapshotView> findAccessSnapshotByUserId(@Bind int userId);
}
