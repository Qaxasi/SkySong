package com.mycompany.SkySong.adapter.identity.deletion.persistence;

import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.application.user.delete.ports.UserDeletion;
import com.mycompany.SkySong.shared.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
class TransactionalUserDeleter implements UserDeletion {

    private final UserDAO userRepository;
    private final TransactionTemplate transactionTemplate;

    TransactionalUserDeleter(UserDAO userDAO,
                             TransactionTemplate transactionTemplate) {
        this.userRepository = userDAO;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void deleteEverythingById(int id) {
        transactionTemplate.executeWithoutResult(status -> {
                    User user = userRepository.findById(id).orElseThrow(
                            () -> {
                                log.warn("Attempted to delete non-existent user with ID {}", id);
                                return new UserNotFoundException(
                                        "User not found with id: " + id,
                                        ErrorType.USER_NOT_FOUND);
                            });

            log.debug("Deleting roles for user with id: {}", user.getId());
            userRepository.deleteUserRoles(id);

            log.debug("Deleting user entity for id: {}", user.getId());
            userRepository.delete(user);
        });
    }
}