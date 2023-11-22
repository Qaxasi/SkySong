package com.mycompany.SkySong.user.controller;

import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.user.service.DeleteUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class DeleteUserController {
    private final DeleteUserService deleteUserService;
    private final ApplicationMessageService messageService;
    public DeleteUserController(DeleteUserService deleteUserService, ApplicationMessageService messageService) {
        this.deleteUserService = deleteUserService;
        this.messageService = messageService;
    }
    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<ApiResponse> delete(@PathVariable(required = false) Long userId) throws DatabaseException {
        if (userId == null) {
            String errorMessage = messageService.getMessage("user.id.required");
            throw new NullOrEmptyInputException(errorMessage);
        }
        ApiResponse deleteResponse = deleteUserService.deleteUser(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
