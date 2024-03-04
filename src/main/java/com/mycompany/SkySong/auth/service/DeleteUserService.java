package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.DatabaseException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class DeleteUserServiceImpl implements DeleteUserService {

    private final DeleteUser deleteUser;
    private final ApplicationMessageService message;

    public DeleteUserServiceImpl(DeleteUser deletionManager, ApplicationMessageService message) {
        this.deleteUser = deletionManager;
        this.message = message;
    }

    @Override
    public ApiResponse deleteUser(int userId) throws DatabaseException {
        deleteUser.delete(userId);
        log.info("User with ID {} deleted successfully", userId);
        return new ApiResponse(message.getMessage("user.delete.success", userId));
    }
}

