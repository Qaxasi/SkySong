package com.mycompany.SkySong.adapter.user.deletion.controller;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.user.delete.usecase.UserDeletionUseCase;
import com.mycompany.SkySong.shared.error.ErrorResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserDeletionController {

    private final UserDeletionUseCase userDeletion;

    public UserDeletionController(final UserDeletionUseCase userDeletion) {
        this.userDeletion = userDeletion;
    }

    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<Object> delete(@PathVariable(required = false) final Integer userId) {
        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse(
                            "User ID is required and cannot be empty.",
                            ErrorType.USER_REQUEST_INVALID.name(),
                            ErrorType.USER_REQUEST_INVALID.getHttpStatus().value()));

        }

        ApiResponse deleteResponse = userDeletion.delete(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
