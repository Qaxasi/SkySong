package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.CookieService;
import com.mycompany.SkySong.auth.service.LoginService;
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
    private final LoginService loginService;
    private final CookieService cookieService;
    public LoginController(LoginService loginService, CookieService cookieService) {
        this.loginService = loginService;
        this.cookieService = cookieService;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        String token = loginService.login(loginRequest);
        ApiResponse jwtAuthResponse = new ApiResponse(token);

        cookieService.addCookie(response, "auth_token", token, 24 * 60 * 60);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
