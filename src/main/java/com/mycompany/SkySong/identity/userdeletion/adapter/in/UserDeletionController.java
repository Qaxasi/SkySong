package com.mycompany.SkySong.identity.userdeletion.adapter.in;

import com.mycompany.SkySong.identity.userdeletion.application.service.UserDeletionHandler;
import com.mycompany.SkySong.shared.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserDeletionController {
    private final UserDeletionHandler userDeletion;
    public UserDeletionController(final UserDeletionHandler userDeletion) {
        this.userDeletion = userDeletion;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable final Integer userId) {

        return userDeletion.deleteUserById(userId)
                .fold(
                        error -> ResponseEntity
                                .status(error.errorType().getHttpStatus())
                                .body(error.toErrorResponse()),

                        success -> ResponseEntity.noContent().build());
    }
}