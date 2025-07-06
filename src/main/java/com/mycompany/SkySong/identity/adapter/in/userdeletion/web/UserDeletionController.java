package com.mycompany.SkySong.identity.adapter.in.userdeletion.web;

import com.mycompany.SkySong.identity.application.userdeletion.service.DeleteUser;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserDeletionController {
    private final DeleteUser deleteUser;

    public UserDeletionController(final DeleteUser userDeletion) {
        this.deleteUser = userDeletion;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable final int userId) {
        final Result<Void> result = deleteUser.execute(userId);

        if (result.isFailure()) {
            return ResponseEntity.status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        return ResponseEntity.noContent().build();
    }
}
