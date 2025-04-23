package com.mycompany.SkySong.application.user.delete.usecase;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.user.delete.ports.UserDeletion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDeletionHandler {

    private final UserDeletion userDeletion;

    public UserDeletionHandler(UserDeletion userDeletion) {
        this.userDeletion = userDeletion;
    }

    public ApiResponse delete(int userId) {
        userDeletion.deleteEverythingById(userId);
        log.info("User with id: {} deleted successfully", userId);
        return new ApiResponse("User deleted successfully.");
    }
}