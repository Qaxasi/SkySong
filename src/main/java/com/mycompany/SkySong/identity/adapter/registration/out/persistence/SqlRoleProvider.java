package com.mycompany.SkySong.identity.adapter.registration.out.persistence;

import com.mycompany.SkySong.identity.application.registration.ports.RoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.sql.RoleDAO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SqlRoleProvider implements RoleProvider {
    private final RoleDAO roleDAO;

    public SqlRoleProvider(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<Role> provideRole(UserRole role) {
        return roleDAO.findByName(role);
    }
}


