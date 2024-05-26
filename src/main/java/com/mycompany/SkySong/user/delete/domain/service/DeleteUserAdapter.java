package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dao.SessionDAO;
import com.mycompany.SkySong.common.dao.UserDAO;
import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.registration.domain.model.User;
import org.springframework.transaction.support.TransactionTemplate;

class DeleteUserPortAdapter implements DeleteUserPort {

    private final UserDAO userRepository;
    private final SessionDAO sessionRepository;
    private final TransactionTemplate transactionTemplate;

    public DeleteUserPortAdapter(UserDAO userDAO,
                                 SessionDAO sessionDAO,
                                 TransactionTemplate transactionTemplate) {
        this.userRepository = userDAO;
        this.sessionRepository = sessionDAO;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void deleteEverythingById(int id) {
        transactionTemplate.executeWithoutResult(status -> {
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                    "User not found with id: " + id));

            userRepository.deleteUserRoles(id);
            sessionRepository.deleteUserSessions(id);

            userRepository.delete(user);
        });
    }
}