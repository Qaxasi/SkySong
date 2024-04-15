package com.mycompany.SkySong.user;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;
import java.util.Set;

public interface RoleDAO {

    @SqlQuery("SELECT * FROM roles WHERE name = :name")
    @RegisterBeanMapper(Role.class)
    Optional<Role> findByName(@Bind("name") UserRole roleName);

    @SqlQuery("SELECT r.name FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId")
    @RegisterBeanMapper(Role.class)
    Set<Role> findRolesByUserId(@Bind("userId") Integer userId);
}
