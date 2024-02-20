package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.DatabaseException;
import com.mycompany.SkySong.auth.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
class DeleteUserServiceImpl implements DeleteUserService {
    private final UserDAO userDAO;
    private final ApplicationMessageService messageService;

    public DeleteUserServiceImpl(UserDAO userDAO, ApplicationMessageService messageService) {
        this.userDAO = userDAO;
        this.messageService = messageService;
    }
    @Transactional
    @Override
    public ApiResponse deleteUser(int userId) throws DatabaseException {
        try {
            return userDAO.findById(userId).map(user -> {
                userDAO.delete(user);
                log.info("User with ID {} deleted successfully", userId);
                return new ApiResponse(messageService.getMessage("user.delete.success", userId));
            }).orElseThrow(() -> {
                log.info("User with ID {} does not exist", userId);
                return new UserNotFoundException(messageService.getMessage("user.delete.not-found", userId));
            });
        } catch (DataAccessException e) {
                throw new DatabaseException(messageService.getMessage("user.delete.error"));
        }
    }
}
