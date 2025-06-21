package com.mycompany.SkySong.identity.adapter.userdeletion.out.persistence;

import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.identity.application.userdeletion.ports.UserDeletion;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.stereotype.Component;
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
    public void deleteEverythingById(final int id) {
        transactionTemplate.executeWithoutResult(status -> {
            logger.debug("Deleting roles for user", context("userId", id));
            userDAO.deleteUserRoles(id);

            logger.debug("Deleting user", context("userId", id));
            userDAO.delete(id);
        });
    }
}