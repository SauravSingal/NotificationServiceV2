package org.example.notificationservicev2.service;


import lombok.AllArgsConstructor;
import org.example.notificationservicev2.dto.RegisterRequest;
import org.example.notificationservicev2.entity.User;
import org.example.notificationservicev2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    public String authRegister (RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            return "Email already exists: " + request.getEmail();
        }
        User user = User.builder().email(request.getEmail()).
                password(request.getPassword()).build();
        userRepository.save(user);
        return "user created";
    }
}
