package org.example.notificationservicev2.controller;


import org.example.notificationservicev2.dto.UserRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationService {

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody UserRequest user) {
        return user.toString();
    }
}
