package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dao.SessionDAO;
import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.common.dao.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUser {

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;

    public DeleteUser(UserDAO userDAO, SessionDAO sessionDAO) {
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
    }

    @Transactional
    public ApiResponse deleteUserById(int userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException(
                "User not found with id: " + userId));

        userDAO.deleteUserRoles(userId);
        sessionDAO.deleteUserSessions(userId);

        userDAO.delete(user);

        return new ApiResponse(String.format("User with ID %d deleted successfully.", userId));
    }
}
