package org.example.notificationservicev2.controller;


import org.example.notificationservicev2.dto.RegisterRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody RegisterRequest user) {
        
        return user.toString();
    }
}
