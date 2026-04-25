package org.example.notificationservicev2.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.notificationservicev2.dto.LoginResponseDTO;
import org.example.notificationservicev2.dto.RegisterRequest;
import org.example.notificationservicev2.entity.User;
import org.example.notificationservicev2.repository.UserRepository;
import org.example.notificationservicev2.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User authRegister (RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UsernameNotFoundException("Email already in use");
        }
        User user = User.builder().email(request.getEmail()).
                password(passwordEncoder.encode(request.getPassword())).build();
        userRepository.save(user);
        return user;
    }

    public LoginResponseDTO authLogin(RegisterRequest user) {
        Authentication authentication = authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        User foundInDB = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(foundInDB);
        return new LoginResponseDTO(token, foundInDB.getEmail(), foundInDB.getId());
    }
}
