package com.jamesbrooker.budgeting.controller;

import com.jamesbrooker.budgeting.service.UserService;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.jamesbrooker.budgeting.model.*;
import com.jamesbrooker.budgeting.security.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.email, request.password);
        String token = jwtUtil.createToken(user.getId().toString(), user.getEmail());
        return new LoginResponse(token);
    }

    static class LoginRequest {
        public String email;
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request.email, request.password);
        UserResponse response = new UserResponse(user.getId(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    static class RegisterRequest {
        @Email
        @NotBlank
        public String email;
        @NotBlank
        @Size(min = 8, max = 100)
        public String password;
    }
}