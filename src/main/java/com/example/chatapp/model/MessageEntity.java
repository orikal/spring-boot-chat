package com.example.chatapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender; // Who sent the message

    @Column(length = 1000) // Support longer messages
    private String content; // The message text

    private LocalDateTime timestamp; // When it was sent

    // Automatically set the timestamp when the message is created
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}