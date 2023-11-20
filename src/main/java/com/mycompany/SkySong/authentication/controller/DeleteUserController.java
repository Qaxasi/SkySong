package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.service.DeleteUserService;
import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;
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
    public ResponseEntity<ApiResponse> delete(@PathVariable(required = false) Long userId) {
        if (userId == null) {
            String errorMessage = messageService.getMessage("user.id.required");
            throw new NullOrEmptyInputException(errorMessage);
        }
        ApiResponse deleteResponse = deleteUserService.deleteUser(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
