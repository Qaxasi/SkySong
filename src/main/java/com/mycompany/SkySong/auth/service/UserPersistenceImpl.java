package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPersistenceImpl implements UserPersistence {

    private final UserDAO userDAO;

    public UserPersistenceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        int userId = userDAO.save(user);

        for (Role role : user.getRoles()) {
            userDAO.assignRoleToUser(userId, role.getId());
        }
    }
}
