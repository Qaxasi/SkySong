package com.mycompany.SkySong.adapter.user.registration.persistence;

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

    public TransactionUserSaver(final UserDAO userDAO,
                                final TransactionTemplate transactionTemplate) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void saveUser(final User user) {
        transactionTemplate.executeWithoutResult(status -> {
            final int userId = userDAO.save(user);

            for (Role roles : user.getRoles()) {
                userDAO.assignRoleToUser(userId, roles.getId());
            }
        });
    }
}


