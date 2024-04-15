package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.user.User;
import com.mycompany.SkySong.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserSaver {

    @Autowired
    private UserDAO userDAO;

    @Transactional
    public void save(User user) {
        int userId = userDAO.save(user);
        user.getRoles().forEach(role -> userDAO.assignRoleToUser(userId, role.getId()));
    }
}
