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
}
