package com.mycompany.SkySong.identity.adapter.registration.out.persistence;

import com.mycompany.SkySong.infrastructure.persistence.exception.PersistenceException;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.application.registration.ports.UserSaver;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
class TransactionUserSaver implements UserSaver {
    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;

    public TransactionUserSaver(final UserDAO userDAO,
                                final TransactionTemplate transactionTemplate) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;

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
            throw new PersistenceException("Error occurred while saving user", ex);
        }
    }
}


