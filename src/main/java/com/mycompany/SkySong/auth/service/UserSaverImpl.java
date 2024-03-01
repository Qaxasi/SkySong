package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserSaverImpl implements UserSaver {

    private final UserDAO userDAO;

    public UserSaverImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void saveUser(User user) {
        int userId = userDAO.save(user);

        for (Role role : user.getRoles()) {
            userDAO.assignRoleToUser(userId, role.getId());
        }
    }
}
