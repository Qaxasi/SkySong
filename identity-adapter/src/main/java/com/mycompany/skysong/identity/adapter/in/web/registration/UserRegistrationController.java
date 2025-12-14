package com.mycompany.skysong.identity.adapter.in.web.registration;

import com.mycompany.skysong.identity.adapter.in.web.common.RawPasswordGuard;
import com.mycompany.skysong.identity.application.user.registration.service.RegisterUser;
import com.mycompany.skysong.identity.domain.Email;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.web.error.ErrorResponse;
import com.mycompany.skysong.web.error.ErrorTypeToHttpStatusMapper;
import com.mycompany.skysong.web.response.ResponsePayload;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserRegistrationController {
    private final RegisterUser registerUser;
    private final RawPasswordGuard passwordGuard;
    public UserRegistrationController(final RegisterUser registerUser,
                                      final RawPasswordGuard passwordGuard) {
        this.registerUser = registerUser;
        this.passwordGuard = passwordGuard;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponsePayload<RegistrationResponse>> register(@Valid @RequestBody final RegistrationRequest request) {
        return Username.fromInput(request.username())
                .flatMap(username -> Email.fromInput(request.email())
                        .flatMap(email ->
                                        passwordGuard.useAndZeroize(
                                                request.password(),
                                                password -> registerUser.register(username, email, password))
                        )
                )
                .fold(error -> {
                    final HttpStatus status = ErrorTypeToHttpStatusMapper.toHttpStatus(error.errorType());
                    return ResponseEntity
                            .status(status)
                            .body(ResponsePayload.error(
                                    new ErrorResponse(
                                            error.message(),
                                            status.name(),
                                            status.value(),
                                            Map.of())));
                    },
                        success ->
                                ResponseEntity
                                        .status(HttpStatus.CREATED)
                                        .body(ResponsePayload.ok(new RegistrationResponse("Registration successful")))
                );
    }
}
