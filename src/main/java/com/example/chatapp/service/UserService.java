// service/UserService.java
package com.example.chatapp.service;

import com.example.chatapp.model.User;
import com.example.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Logic for creating a NEW user
    public User registerUser(User user) throws Exception {
        // Check if username already exists in Database
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new Exception("Username already taken!");
        }
        return userRepository.save(user); // Save to H2
    }

    // Logic for authenticating an EXISTING user
    public User loginUser(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new Exception("Invalid username or password!");
    }
}