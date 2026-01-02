package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIdentityDAO {
    @SqlQuery("""
            SELECT id
            FROM last_admin_delete_guard
            WHERE id = 1
            FOR UPDATE
            """)
    void lockLastAdminInvariantGuard();

    @SqlUpdate("DELETE FROM users WHERE id = :userId")
    int deleteUserById(@Bind("userId") int userId);

    @RegisterConstructorMapper(DeleteUserPrecheckProjection.class)
    @SqlQuery("""
          SELECT
            EXISTS (
              SELECT 1 FROM users
              WHERE id = :userId
              ) AS userExists,
            EXISTS (
              SELECT 1 FROM user_roles
              WHERE user_id = :userId
                AND role_code = 'ADMIN'
                ) AS isAdmin,
            EXISTS (
              SELECT 1 FROM user_roles
              WHERE role_code = 'ADMIN'
                AND user_id <> :userId
                ) AS otherAdminExists
          """)
    DeleteUserPrecheckProjection getDeleteUserPrecheck(@Bind("userId") int userId);

    @SqlUpdate("""
          INSERT INTO users (username, email, password_hash, user_tag)
          VALUES (:username, :email, :passwordHash, :userTag)
           """)
    @GetGeneratedKeys("id")
    int saveUser(@BindBean NewUserParams row);

    @SqlUpdate("""
            INSERT INTO user_roles (user_id, role_code)
            VALUES (:userId, :roleCode)
            """)
    void assignRole(@BindBean RoleAssignmentParams row);

    @SqlQuery("""
            SELECT id AS userId,
            username,
            passwordHash
            FROM users
            WHERE username = :username
            """)
    @RegisterConstructorMapper(UserAuthProjection.class)
    Optional<UserAuthProjection> findAuthByUsername(@Bind String username);

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
    Optional<UserAccessSnapshotProjection> findUserAccessSnapshotByUserId(@Bind int userId);

    @SqlQuery("""
            SELECT
                EXISTS (SELECT 1 FROM users WHERE username = :username) AS usernameExists,
                EXISTS (SELECT 1 FROM users WHERE email = :email) AS emailExists
             """)
    @RegisterConstructorMapper(UniquenessStatusView.class)
    UniquenessStatusView checkUniqueness(@Bind("username") String username,
                                         @Bind("email") String email);
}
