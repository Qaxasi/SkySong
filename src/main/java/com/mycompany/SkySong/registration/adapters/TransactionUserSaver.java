package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.common.dao.UserDAO;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionUserSaver implements UserSaver {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final TransactionTemplate transactionTemplate;

    public TransactionUserSaver(UserDAO userDAO, RoleDAO roleDAO, TransactionTemplate transactionTemplate) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
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
