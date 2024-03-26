package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeleteUserService {

    private final DeleteUser delete;
    private final ApplicationMessageLoader message;

    public DeleteUserService(DeleteUser deletionManager,
                             ApplicationMessageLoader message) {
        this.delete = deletionManager;
        this.message = message;
    }

    public ApiResponse deleteUser(int userId) {
        delete.deleteUserById(userId);
        log.info("User with ID {} deleted successfully", userId);
        return new ApiResponse(message.getMessage("user.delete.success", userId));
    }
}

