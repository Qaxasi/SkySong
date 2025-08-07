package com.mycompany.SkySong.identity.adapter.out.db;

import com.mycompany.SkySong.identity.adapter.out.db.exception.RoleProviderPersistenceException;
import com.mycompany.SkySong.identity.registration.application.port.RoleProvider;
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
    public Optional<Role> provideRole(final UserRole role) {
        try {
            return roleDAO.findByName(role);
        } catch (RuntimeException ex) {
            if (ex instanceof NullPointerException || ex instanceof IllegalArgumentException) {
                throw ex;
            }
            throw new RoleProviderPersistenceException("Error occurred while loading role", ex);
        }
    }
}


