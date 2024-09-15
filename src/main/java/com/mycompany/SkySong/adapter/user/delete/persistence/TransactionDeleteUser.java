package com.mycompany.SkySong.adapter.user.delete.integration;

import com.mycompany.SkySong.adapter.user.delete.integration.exception.UserNotFoundException;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.user.delete.ports.DeleteUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class TransactionDeleteUser implements DeleteUser {

    private final UserDAO userRepository;
    private final TransactionTemplate transactionTemplate;

    TransactionDeleteUser(UserDAO userDAO,
                          TransactionTemplate transactionTemplate) {
        this.userRepository = userDAO;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void deleteEverythingById(int id) {
        transactionTemplate.executeWithoutResult(status -> {
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                    "User not found with id: " + id));

            userRepository.deleteUserRoles(id);
            userRepository.delete(user);
        });
    }
}