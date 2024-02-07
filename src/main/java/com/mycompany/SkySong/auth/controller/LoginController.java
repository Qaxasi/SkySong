package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LoginController {

    private final LoginService login;

    public LoginController(LoginService login) {
        this.login = login;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        String sessionToken = login.login(loginRequest);

        Cookie sessionCookie = new Cookie("session_id", sessionToken);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        return ResponseEntity.ok(new ApiResponse("Logged successfully"));
    }
}
