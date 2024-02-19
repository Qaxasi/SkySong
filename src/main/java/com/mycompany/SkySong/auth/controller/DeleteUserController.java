package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.exception.DatabaseException;
import com.mycompany.SkySong.auth.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.service.ApplicationMessageService;
import com.mycompany.SkySong.auth.service.DeleteUserService;
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
