package com.mycompany.SkySong.identity.registration.adapter.out;

import com.mycompany.SkySong.identity.registration.domain.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.identity.registration.domain.Role;
import com.mycompany.SkySong.identity.registration.application.port.UserSaver;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

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

                for (Role roles : user.getRoles()) {
                    userDAO.assignRoleToUser(userId, roles.getId());
                }
            });
            return Result.success();
        } catch (DataAccessException | TransactionException ex) {
            logger.error("Error while saving user", ex);
            return Result.failure("Unexpected error while saving user", ErrorType.PERSISTENCE_ERROR);
        }
    }
}


