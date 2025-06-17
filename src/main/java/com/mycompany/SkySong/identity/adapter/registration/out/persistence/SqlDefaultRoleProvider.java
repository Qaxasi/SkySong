package com.mycompany.SkySong.identity.adapter.registration.out.persistence;

import com.mycompany.SkySong.identity.application.registration.exception.UserRoleConfigurationException;
import com.mycompany.SkySong.identity.application.registration.ports.DefaultRoleProvider;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.sql.RoleDAO;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.stereotype.Component;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SqlDefaultRoleProvider implements DefaultRoleProvider {
    private final RoleDAO roleDAO;
    private final ApplicationLogger logger;

    public SqlDefaultRoleProvider(final RoleDAO roleDAO,
                                  final ApplicationLogger logger) {
        this.roleDAO = roleDAO;
        this.logger = logger;
    }

    @Override
    public Role provideDefaultRole() {
        return roleDAO.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> {
                    logger.error("Default user role missing in database configuration",
                            context("role", UserRole.ROLE_USER.name()));
                    return new UserRoleConfigurationException(
                            "Default user role not found in the system.",
                            ErrorType.IDENTITY_CONFIGURATION_ERROR);
                });
    }
}


