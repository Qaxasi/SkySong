package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUser {

    private final UserDAO userDAO;

    public DeleteUser(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public void delete(int userId) {
        userDAO.deleteUserRoles(userId);
        userDAO.deleteSessionsByUserId(userId);

        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException(
                "User not found with id: " + userId));

        userDAO.delete(user);
    }
}
