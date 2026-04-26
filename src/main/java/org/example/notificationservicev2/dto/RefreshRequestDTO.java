package org.example.notificationservicev2.dto;

import lombok.Data;

@Data
public class RefreshRequestDTO {
    private Long userID;
    private String refreshToken;
    private String token;
}
