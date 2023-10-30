package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteServiceImpl implements DeleteService {
    @Autowired
    private UserDAO userDAO;
    @Override
    public String deleteUser(long userId) {
        return null;
    }
}
