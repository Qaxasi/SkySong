package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.DatabaseException;
import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.DeleteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DeleteUserServiceImpl implements DeleteUserService {
    private final UserDAO userDAO;

    public DeleteUserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Transactional
    @Override
    public ApiResponse deleteUser(long userId) {
        try {
            return userDAO.findById(userId).map(user -> {
                userDAO.delete(user);
                log.info("User with ID {} deleted successfully", userId);
                return new ApiResponse("User with ID: " + userId + " deleted successfully.");
            }).orElseThrow(() -> {
                log.info("User with ID {} does not exist", userId);
                return new UserNotFoundException(
                        "User with ID: " + userId + " does not exist."
                );
            });
        } catch (DataAccessException e) {
                throw new DatabaseException(
                        "An unexpected error occurred while deleting user. Please try again later.");
        }
    }
}
