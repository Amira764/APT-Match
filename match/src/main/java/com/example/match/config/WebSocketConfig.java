package com.example.match.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
//TODO: write the websocket configurations
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    // #fixed bug: Added withSockJS() for fallback options
    registry.addEndpoint("/ws").withSockJS(); // WebSocket endpoint
}

@Override
public void configureMessageBroker(MessageBrokerRegistry registry) {
    // #fixed bug: Confirmed prefixes match client expectations
    registry.setApplicationDestinationPrefixes("/app"); // Messages sent from client go here
    registry.enableSimpleBroker("/topic"); // Server sends to clients via /topic
}
}