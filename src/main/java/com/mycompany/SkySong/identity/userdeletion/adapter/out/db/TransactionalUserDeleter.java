package com.mycompany.SkySong.identity.userdeletion.adapter.out.db;

import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.identity.userdeletion.application.port.UserDeletion;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
class TransactionalUserDeleter implements UserDeletion {
    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationLogger logger;

    TransactionalUserDeleter(final UserDAO userDAO,
                             final TransactionTemplate transactionTemplate,
                             final ApplicationLogger logger) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
        this.logger = logger;
    }

    @Override
    public Result<Void> deleteEverythingById(final int id) {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                logger.debug("Deleting roles for user", context("userId", id));
                userDAO.deleteUserRoles(id);

                logger.debug("Deleting user", context("userId", id));
                userDAO.delete(id);

            });
            return Result.success();
        } catch (DataAccessException | TransactionException ex) {
            logger.error("Database error while deleting user", context("userId", id), ex);
            return Result.failure("An unexpected error occurred while deleting user", ErrorType.DATABASE_ERROR);
        }
    }
}