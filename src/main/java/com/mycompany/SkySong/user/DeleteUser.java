package com.mycompany.SkySong.user;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
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
    public void deleteUserById(int userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException(
                "User not found with id: " + userId));

        userDAO.deleteUserRoles(userId);
        userDAO.deleteUserSessions(userId);

        userDAO.delete(user);
    }
}
