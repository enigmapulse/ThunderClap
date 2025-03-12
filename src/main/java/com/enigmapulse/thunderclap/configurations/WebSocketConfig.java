package com.enigmapulse.thunderclap.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Register the WebSocket endpoint that clients will use to connect.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //this is the first endpoint which any client must send a get request to upgrade the connection from
        //a simple http protocol to websockets to allow duplex communication
        registry.addEndpoint("/ws").withSockJS();
    }

    // Configure the message broker that will route messages from one client to another.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enables a simple in-memory broker with a destination prefix
        registry.enableSimpleBroker("/topic", "/queue");

        // Prefix for messages bound for methods annotated with @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");

        // Enables user-specific messaging (for private chats)
        registry.setUserDestinationPrefix("/user");
    }
}