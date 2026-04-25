package org.example.notificationservicev2.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Email
    String email;
    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
    String password;


}
