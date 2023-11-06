package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.DeleteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DeleteServiceImpl implements DeleteService {
    private final UserDAO userDAO;

    public DeleteServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Transactional
    @Override
    public DeleteResponse deleteUser(long userId) {
        return userDAO.findById(userId).map(user -> {
            userDAO.delete(user);
            log.error("User with ID {} deleted successfully", userId);
            return new DeleteResponse("User with ID: " + userId + " deleted successfully.");
        }).orElseThrow(() -> {
            log.error("User with ID {} does not exist", userId);
            return new UserNotFoundException(
                    "User with ID: " + userId + " does not exist."
            );
        });
    }
}
