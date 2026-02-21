package com.example.chatapp.model;

import lombok.Data;

@Data // Generates getters, setters, and toString automatically
public class ChatMessage {
    private String content; // The message text
    private String sender;  // The username of the sender
    private MessageType type; // To distinguish between JOIN and CHAT messages

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}