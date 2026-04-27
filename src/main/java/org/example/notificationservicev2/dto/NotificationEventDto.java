package org.example.notificationservicev2.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEventDto {
    private Long userID;
    private String type; //EMAIL or IN_APP
    private String message;
    private String title;

}
