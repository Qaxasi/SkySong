package com.mycompany.SkySong.infrastructure.persistence.dao;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.login.ports.LoginUserRepository;
import com.mycompany.SkySong.domain.registration.ports.RegistrationUserRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends LoginUserRepository, RegistrationUserRepository {

    @SqlUpdate("INSERT INTO users (username, email, password) VALUES (:username, :email, :password)")
    @GetGeneratedKeys
    int save(@BindBean User user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    @RegisterBeanMapper(User.class)
    Optional<User> findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    @RegisterBeanMapper(User.class)
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    @RegisterBeanMapper(User.class)
    Optional<User> findByUsername(@Bind("username") String username);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM users WHERE username = :username)")
    boolean existsByUsername(@Bind("username") String username);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM users WHERE email = :email)")
    boolean existsByEmail(@Bind("email") String email);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void delete(@BindBean User user);

    @SqlUpdate("DELETE FROM user_roles WHERE user_id = :userId")
    void deleteUserRoles(@Bind("userId") int userId);

    @SqlUpdate("INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
    void assignRoleToUser(@Bind("userId") int userId, @Bind("roleId") int roleId);
}