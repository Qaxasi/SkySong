package com.mycompany.SkySong.infrastructure.persistence.sql;

import com.mycompany.SkySong.identity.registration.application.dto.UniquenessStatus;
import com.mycompany.SkySong.identity.registration.domain.User;
import com.mycompany.SkySong.identity.registration.application.port.RegistrationUserRepository;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends RegistrationUserRepository {

    @SqlUpdate("INSERT INTO users (username, email, password) VALUES (:username, :email, :password)")
    @GetGeneratedKeys
    int save(@BindBean User user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    Optional<User> findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Bind("username") String username);

    @SqlQuery("""
            INSERT INTO user_roles (user_id, role_id)
            SELECT :userId, r.id
            FROM roles r
            WHERE r.name = :roleName
            ON DUPLICATE KEY UPDATE role_id = role_id 
            """)
    int assignRoleToUserByName(@Bind("userId") int userId,
                               @Bind("roleName") String roleName);

    @SqlQuery("""
            SELECT
                EXISTS(SELECT 1 FROM users WHERE username = :username) AS usernameExists,
                EXISTS(SELECT 1 FROM users WHERE email = :email) AS emailExists
                   """)
    UniquenessStatus checkUniqueness(@Bind("username") String username,
                                     @Bind("email") String email);
    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void delete(@Bind("id") int id);

    @SqlUpdate("DELETE FROM user_roles WHERE user_id = :userId")
    void deleteUserRoles(@Bind("userId") int userId);

    @SqlUpdate("INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
    void assignRoleToUser(@Bind("userId") int userId, @Bind("roleId") int roleId);
}