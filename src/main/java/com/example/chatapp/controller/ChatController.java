package com.example.chatapp.controller;

import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.model.MessageEntity;
import com.example.chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;



@Controller // This is a specialized controller for handling messages
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    // This method handles messages sent to "/app/chat.sendMessage"
    @MessageMapping("/chat.sendMessage")
    // The return value is broadcast to all subscribers of "/topic/public"
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // Create an entity to save in the database
        MessageEntity entity = new MessageEntity();
        entity.setSender(chatMessage.getSender());
        entity.setContent(chatMessage.getContent());

        messageRepository.save(entity);
        return chatMessage;
    }

    // This method handles "JOIN" events when a user enters the chat
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session attributes
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}