package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.common.dao.UserDAO;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.ports.UserPersistence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class TransactionUserPersistence implements UserPersistence {

    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;

    public TransactionUserPersistence(UserDAO userDAO,
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
