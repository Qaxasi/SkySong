package com.mycompany.SkySong.adapter.user.deletion.controller;

import com.mycompany.SkySong.application.user.delete.usecase.UserDeletionUseCase;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.shared.result.Result;

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
    public ResponseEntity<BaseResponse> delete(@PathVariable(required = false) final Integer userId) {
        Result<SuccessResponse> result = userDeletion.delete(userId);

        if (result.isFailure()) {
            return ResponseEntity.status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        return ResponseEntity.ok(result.data());
    }
}
