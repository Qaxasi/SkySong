package com.mycompany.SkySong.adapter.user.registration.persistence;

import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.application.user.registration.ports.UserSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
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
        transactionTemplate.executeWithoutResult(status -> {
            try {
                final int userId = userDAO.save(user);

                for (Role roles : user.getRoles()) {
                    userDAO.assignRoleToUser(userId, roles.getId());
                }
            } catch (final RuntimeException ex) {
                logger.error("Failed to save user", ex);
                throw ex;
            }
        });
    }
}
