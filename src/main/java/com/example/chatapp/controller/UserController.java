package com.example.chatapp.controller;

import com.example.chatapp.model.MessageEntity;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.MessageRepository;
import com.example.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "The server is running";
    }

    @GetMapping("/messages") // Endpoint to fetch chat history
    public List<MessageEntity> getChatHistory() {
        // Fetches messages from DB ordered by timestamp (Oldest to Newest)
        return messageRepository.findTop50ByOrderByTimestampAsc();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            // Return JSON: {"error": "Username already taken!"}
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(authenticatedUser);
        } catch (Exception e) {
            // Return JSON: {"error": "Invalid username or password!"}
            return ResponseEntity.status(401).body(java.util.Map.of("error", e.getMessage()));
        }
    }
}