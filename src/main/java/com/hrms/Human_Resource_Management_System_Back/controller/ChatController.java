package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket controller for handling chat-related messaging within the application.
 * <p>
 * This controller listens for messages sent to specific STOMP destinations, processes them,
 * and broadcasts the responses to all subscribers. It manages sending chat messages and
 * registering new users in the WebSocket session.
 * </p>
 */
@Controller
public class ChatController {

    /**
     * Handles the event when a user sends a chat message.
     * <p>
     * This method receives a {@link ChatMessage} payload from the "/app/chat.sendMessage" destination.
     * It retrieves the sender's username from the WebSocket session and attaches it to the message.
     * Then, it broadcasts the message to all users subscribed to "/topic/public".
     * </p>
     *
     * @param chatMessage     the message payload sent by the client
     * @param headerAccessor  provides access to WebSocket session attributes
     * @return the enriched {@link ChatMessage} that will be sent to all subscribers
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        chatMessage.setSender(username);
        return chatMessage;
    }

    /**
     * Handles the event when a new user joins the chat.
     * <p>
     * This method receives a {@link ChatMessage} from "/app/chat.addUser" destination.
     * It stores the sender's username in the WebSocket session attributes, allowing tracking
     * of the user during the session. The message is then broadcast to all subscribers.
     * </p>
     *
     * @param chatMessage     the message indicating a new user has joined
     * @param headerAccessor  provides access to WebSocket session attributes
     * @return the same {@link ChatMessage}, which gets broadcast to notify others
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username to WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
