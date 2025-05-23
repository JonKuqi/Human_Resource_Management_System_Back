package com.hrms.Human_Resource_Management_System_Back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for enabling and setting up WebSocket communication in the application.
 * <p>
 * This class configures a STOMP-over-WebSocket message broker for enabling real-time messaging
 * between users within the Human Resource Management System (HRMS). It establishes the WebSocket
 * endpoints and configures message routing for client-server communication.
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP (Simple Text Oriented Messaging Protocol) endpoints used for WebSocket communication.
     * <p>
     * This method exposes the "/ws" endpoint to be used by the front-end clients for establishing WebSocket connections.
     * The `withSockJS()` fallback enables support for clients that do not support native WebSockets.
     * `setAllowedOriginPatterns("*")` allows cross-origin requests from any source (can be restricted in production).
     * </p>
     *
     * @param registry the {@link StompEndpointRegistry} used to register WebSocket STOMP endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * Configures the message broker used for routing messages between clients and the server.
     * <p>
     * The configuration uses a simple in-memory broker to relay messages to subscribers on destinations
     * prefixed with "/topic". Application destination prefixes ("/app") are used to map messages
     * to @MessageMapping methods within controllers.
     * </p>
     *
     * @param registry the {@link MessageBrokerRegistry} for setting up messaging routes.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Routes from client to server methods
        registry.enableSimpleBroker("/topic");              // Routes from server to subscribed clients
    }

}
