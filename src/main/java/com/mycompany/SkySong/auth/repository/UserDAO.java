package com.mycompany.SkySong.auth.repository;

import com.mycompany.SkySong.auth.model.entity.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface UserDAO {

    @SqlUpdate("INSERT INTO users (username, email, password) VALUES (:username, :email, :password)")
    void save(@BindBean User user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    @RegisterBeanMapper(User.class)
    Optional<User> findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    @RegisterBeanMapper(User.class)
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    @RegisterBeanMapper(User.class)
    Optional<User> findByUsername(@Bind("username") String username);

    @SqlQuery("SELECT COUNT(*) FROM users WHERE username = :username")
    int existsByUsername(@Bind("username") String username);

    @SqlQuery("SELECT COUNT(*) FROM users WHERE email = :email")
    int existsByEmail(@Bind("email") String email);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void delete(@BindBean User user);
}