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

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    int deleteUserById(@Bind("id") int id);

    @SqlQuery("""
          SELECT
            (
             EXISTS (
               SELECT 1
               FROM user_roles ur
               WHERE ur.user_id = u.id
                 AND ur.role_code = 'ADMIN'
              )
              AND NOT EXISTS (
                SELECT 1
                FROM user_roles ur2
                WHERE ur2.role_code = 'ADMIN'
                AND ur2.user_id <> u.id
              )
            ) AS isLastAdmin
          FROM users u
          WHERE u.id = :userId
          """)
    Optional<LastAdminProjection> isLastAdminByUserId(@Bind("userId") int userId);

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
