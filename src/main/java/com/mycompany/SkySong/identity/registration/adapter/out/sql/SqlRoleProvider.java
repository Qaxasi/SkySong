package com.mycompany.SkySong.identity.registration.adapter.out;

import com.mycompany.SkySong.identity.registration.application.port.RoleProvider;
import com.mycompany.SkySong.identity.registration.domain.Role;
import com.mycompany.SkySong.identity.registration.domain.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.sql.RoleDAO;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SqlRoleProvider implements RoleProvider {
    private final RoleDAO roleDAO;
    private final ApplicationLogger logger;

    public SqlRoleProvider(final RoleDAO roleDAO,
                           final ApplicationLogger logger) {
        this.roleDAO = roleDAO;
        this.logger = logger;
    }

    @Override
    @Cacheable(cacheNames = "roles", key = "#role.name()", unless = "#result == null || #result.isFailure()")
    public Result<Role> provideRole(final UserRole role) {
        try {
            final Optional<Role> optionalRole = roleDAO.findByName(role);
            return optionalRole.map(Result::success).orElseGet(
                    () -> Result.failure("Role not found", ErrorType.ROLE_NOT_FOUND));
        } catch (DataAccessException ex) {
            logger.error("Database error while fetching role", context("role", role), ex);
            return Result.failure("Unexpected error occurred while fetching role", ErrorType.DATABASE_ERROR);
        }
    }
}


