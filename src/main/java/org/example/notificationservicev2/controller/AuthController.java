package org.example.notificationservicev2.controller;

import lombok.AllArgsConstructor;
import org.example.notificationservicev2.dto.RegisterRequest;
import org.example.notificationservicev2.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    AuthService authService;
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest user) {

    return authService.authRegister(user);
    }
}
