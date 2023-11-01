package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class DeleteServiceImpl implements DeleteService {
    private final UserDAO userDAO;

    public DeleteServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public DeleteResponse deleteUser(long userId) {

        Optional<User> user = userDAO.findById(userId);

        if (user.isPresent()) {
            userDAO.delete(user.get());
            return new DeleteResponse("User deleted successfully.");
        } else {
            return new DeleteResponse("User with ID: " + userId + " does not exist.");
        }
    }
}
