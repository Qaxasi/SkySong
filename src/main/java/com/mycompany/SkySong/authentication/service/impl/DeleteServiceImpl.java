package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.DeleteService;
import org.springframework.stereotype.Service;

@Service
public class DeleteServiceImpl implements DeleteService {
    private final UserDAO userDAO;

    public DeleteServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public DeleteResponse deleteUser(long userId) {

        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException(
                "User with ID: " + userId + " does not exist."));

        userDAO.delete(user);
        return new DeleteResponse("User with ID: " + userId + " does not exist.");
    }
}
