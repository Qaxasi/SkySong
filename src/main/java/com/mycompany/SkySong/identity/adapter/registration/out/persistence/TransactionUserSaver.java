package com.mycompany.SkySong.identity.adapter.registration.out.persistence;

import com.mycompany.SkySong.identity.application.exception.IdentityApplicationException;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.application.registration.ports.UserSaver;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
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
    public void saveUser(final User user) {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                final int userId = userDAO.save(user);

                for (Role roles : user.getRoles()) {
                    userDAO.assignRoleToUser(userId, roles.getId());
                }
            });
        } catch (DataAccessException ex) {
            logger.error("Unexpected error while saving user to database", ex);
            throw new IdentityApplicationException(
                    "Error occurred while saving user",
                    ErrorType.PERSISTENCE_ERROR);
        }
    }
}


