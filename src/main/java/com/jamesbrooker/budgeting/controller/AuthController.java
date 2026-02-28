package com.jamesbrooker.budgeting.controller;

import com.jamesbrooker.budgeting.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.jamesbrooker.budgeting.model.*;
import com.jamesbrooker.budgeting.security.*;

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
}