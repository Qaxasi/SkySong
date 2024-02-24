package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserPersistenceImpl implements UserPersistence {

    private final UserDAO userDAO;

    public UserPersistenceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void saveUser(User user) {

    }
}
