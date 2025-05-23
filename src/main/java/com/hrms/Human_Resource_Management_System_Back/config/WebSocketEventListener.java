package com.hrms.Human_Resource_Management_System_Back.config;

import com.hrms.Human_Resource_Management_System_Back.model.dto.ChatMessage;
import com.hrms.Human_Resource_Management_System_Back.model.types.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Event listener component for handling WebSocket connection lifecycle events.
 * <p>
 * This component listens for WebSocket disconnection events and broadcasts a leave message to all connected clients,
 * notifying them that a user has disconnected from the public chat.
 * </p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    /**
     * Template for sending messages through WebSocket to specified destinations.
     * Used to broadcast messages when a user disconnects.
     */
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Handles WebSocket session disconnection events.
     * <p>
     * This method is triggered when a user disconnects from a WebSocket session. It retrieves the username from the
     * session attributes (if present), logs the disconnection, constructs a {@link ChatMessage} of type {@link MessageType#LEAVE},
     * and sends the message to the "/topic/public" topic to notify other users.
     * </p>
     *
     * @param event the {@link SessionDisconnectEvent} triggered upon a user's disconnection.
     */

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User disconnected: {}", username);

            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
