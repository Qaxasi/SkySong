package com.mycompany.SkySong.identity.adapter.registration.out.persistence;

import com.mycompany.SkySong.identity.application.registration.exception.UserRoleConfigurationException;
import com.mycompany.SkySong.identity.application.registration.ports.DefaultRoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.sql.RoleDAO;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SqlDefaultRoleProvider implements DefaultRoleProvider {
    private final RoleDAO roleDAO;

    public SqlDefaultRoleProvider(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<Role> provideDefaultRole() {
        return roleDAO.findByName(UserRole.ROLE_USER);
    }
}


