package org.example.notificationservicev2.controller;

import lombok.AllArgsConstructor;
import org.example.notificationservicev2.dto.LoginResponseDTO;
import org.example.notificationservicev2.dto.RefreshRequestDTO;
import org.example.notificationservicev2.dto.RegisterRequest;
import org.example.notificationservicev2.entity.User;
import org.example.notificationservicev2.service.AuthService;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    return new LoginResponseDTO("Login to generate token","Login to generate token", response.getEmail(), response.getId());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody RegisterRequest user) {
        return  authService.authLogin(user);
    }

    @PostMapping("/refresh")
    public LoginResponseDTO refresh(@RequestBody RefreshRequestDTO request) {
        return authService.authRefresh(request);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody RefreshRequestDTO request) {
        return authService.deleteRefreshToken(request);
    }

}
