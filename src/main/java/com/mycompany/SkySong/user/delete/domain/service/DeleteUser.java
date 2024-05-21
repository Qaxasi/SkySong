package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.user.delete.domain.ports.SessionRepositoryPort;
import com.mycompany.SkySong.user.delete.domain.ports.UserRepositoryPort;

class DeleteUser {

    private final UserRepositoryPort userRepository;
    private final SessionRepositoryPort sessionRepository;

    DeleteUser(UserRepositoryPort userRepository,
               SessionRepositoryPort sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    ApiResponse deleteUserById(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                "User not found with id: " + userId));

        userRepository.deleteUserRoles(userId);
        sessionRepository.deleteUserSessions(userId);

        userRepository.delete(user);

        return new ApiResponse(String.format("User with ID %d deleted successfully.", userId));
    }
}
