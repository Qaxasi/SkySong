package com.mycompany.SkySong.auth.repository;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;

public interface RoleDAO {

    @SqlQuery("SELECT * FROM roles WHERE name = :name")
    @RegisterBeanMapper(Role.class)
    Optional<Role> findByName(@Bind("name") UserRole roleName);
}
