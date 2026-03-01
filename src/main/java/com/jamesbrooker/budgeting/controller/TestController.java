package com.jamesbrooker.budgeting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(Authentication auth) {
        return "User: " + auth.getPrincipal();
    }
}