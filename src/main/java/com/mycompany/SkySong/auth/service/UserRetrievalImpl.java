package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRetrievalImpl implements UserRetrieval {

    private final UserDAO userDAO;

    public UserRetrievalImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User findByAuthUsername(String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));
    }
}
