package org.example.notificationservicev2.entity;


import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "users")
//as user table name is not allowed
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String message;
    @Column(nullable = false)
    String messageType;
}
