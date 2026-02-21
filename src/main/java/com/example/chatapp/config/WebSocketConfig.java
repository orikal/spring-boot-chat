package com.example.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint where the clients will connect to the WebSocket server
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Messages starting with "/app" are routed to @MessageMapping methods in controllers
        registry.setApplicationDestinationPrefixes("/app");

        // Messages starting with "/topic" are routed back to the clients (broadcast)
        registry.enableSimpleBroker("/topic");
    }
}