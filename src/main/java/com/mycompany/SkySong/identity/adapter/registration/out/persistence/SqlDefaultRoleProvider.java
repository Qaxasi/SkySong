package com.mycompany.SkySong.identity.adapter.registration.out.persistence;

import com.mycompany.SkySong.identity.application.registration.ports.DefaultRoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.sql.RoleDAO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SqlDefaultRoleProvider implements DefaultRoleProvider {
    private final RoleDAO roleDAO;

    public SqlDefaultRoleProvider(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<Role> provideRole(UserRole role) {
        return roleDAO.findByName(role);
    }
}


