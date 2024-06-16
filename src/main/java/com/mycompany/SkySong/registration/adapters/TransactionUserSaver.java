package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.common.entity.Role;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class TransactionUserSaver implements UserSaver {

    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;

    public TransactionUserSaver(UserDAO userDAO,
                                TransactionTemplate transactionTemplate) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void saveUser(User user) {
        transactionTemplate.executeWithoutResult(status -> {
            int userId = userDAO.save(user);

            for (Role roles : user.getRoles()) {
                userDAO.assignRoleToUser(userId, roles.getId());
            }
        });
    }
}
