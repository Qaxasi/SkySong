package com.mycompany.SkySong.identity.registration.adapter.out.sql;

import com.mycompany.SkySong.identity.registration.domain.User;
import com.mycompany.SkySong.identity.registration.domain.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.identity.registration.application.port.UserSaver;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
class TransactionUserSaver implements UserSaver {
    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationLogger logger;

    public TransactionUserSaver(final UserDAO userDAO,
                                final TransactionTemplate transactionTemplate,
                                final ApplicationLogger logger) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
        this.logger = logger;
    }

    @Override
    public Result<Void> saveUser(final User user) {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                final int userId = userDAO.save(user);

                for (UserRole role : user.getRoles()) {
                    int rows = userDAO.assignRoleToUserByName(userId, role.name());
                    if (rows == 0) {
                        throw new MissingRoleSeedException(role);
                    }
                }
            });
            return Result.success();
        } catch (MissingRoleSeedException ex) {
            logger.error("Required role missing in database", context("role", ex.role()));
            return Result.failure("Registration temporarily unavailable", ErrorType.PERSISTENCE_ERROR);

        } catch (DataAccessException | TransactionException ex) {
            logger.error("Error while saving user", context("username", user.getUsername()), ex);
            return Result.failure("Unexpected error while saving user", ErrorType.PERSISTENCE_ERROR);
        }
    }
}


