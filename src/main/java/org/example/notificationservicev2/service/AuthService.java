package org.example.notificationservicev2.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.notificationservicev2.dto.RegisterRequest;
import org.example.notificationservicev2.entity.User;
import org.example.notificationservicev2.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    public User authRegister (RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            //return UsernameNotFoundException;
        }
        User user = User.builder().email(request.getEmail()).
                password(request.getPassword()).build();
        userRepository.save(user);
        return user;
    }

    public User authLogin(RegisterRequest user) {
        Authentication authentication = authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        User foundInDB = (User) authentication.getPrincipal();

        return foundInDB;
    }
}
