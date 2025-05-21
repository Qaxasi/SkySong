package com.mycompany.SkySong.adapter.user.deletion.persistence;

import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.application.user.delete.ports.UserDeletion;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
class TransactionalUserDeleter implements UserDeletion {

    private final UserDAO userRepository;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationLogger logger;

    TransactionalUserDeleter(final UserDAO userDAO,
                             final TransactionTemplate transactionTemplate,
                             final ApplicationLogger logger) {
        this.userRepository = userDAO;
        this.transactionTemplate = transactionTemplate;
        this.logger = logger;
    }

    @Override
    public void deleteEverythingById(final int id) {
        transactionTemplate.executeWithoutResult(status -> {
                   final User user = userRepository.findById(id).orElseThrow(
                            () -> new UserNotFoundException(String.format("User not found with id = %d ", id)));

            logger.debug("Deleting roles for user", context("userId", id));
            userRepository.deleteUserRoles(id);

            logger.debug("Deleting user entity from DB", context("userId", id));
            userRepository.delete(user);
        });
    }
}