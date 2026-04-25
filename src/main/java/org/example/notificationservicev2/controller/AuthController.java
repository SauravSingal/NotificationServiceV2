package org.example.notificationservicev2.controller;

import lombok.AllArgsConstructor;
import org.example.notificationservicev2.dto.LoginResponseDTO;
import org.example.notificationservicev2.dto.RegisterRequest;
import org.example.notificationservicev2.entity.User;
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
    public LoginResponseDTO register(@RequestBody RegisterRequest user) {
    User response =  authService.authRegister(user);
    return new LoginResponseDTO("token", response.getEmail(), response.getId());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody RegisterRequest user) {
        return  authService.authLogin(user);
    }

}
