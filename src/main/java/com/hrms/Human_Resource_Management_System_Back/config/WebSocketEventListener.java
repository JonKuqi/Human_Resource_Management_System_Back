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
 * This component listens for WebSocket disconnection events and broadcasts a leave message to
 * all connected clients within the same tenant. It ensures that real-time chat communication
 * is isolated per tenant, so that users from one tenant do not receive events from another.
 * </p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    /**
     * Template for sending messages through WebSocket to specified destinations.
     * Used to broadcast messages within the appropriate tenant topic when a user disconnects.
     */
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Handles WebSocket session disconnection events.
     * <p>
     * This method is triggered when a user disconnects from a WebSocket session. It retrieves
     * the username and tenant ID from the session attributes, logs the disconnection,
     * constructs a {@link ChatMessage} of type {@link MessageType#LEAVE},
     * and sends the message to the corresponding tenant-specific topic
     * (e.g., <code>/topic/tenant-{tenant}</code>) to notify other users from the same tenant.
     * </p>
     *
     * @param event the {@link SessionDisconnectEvent} triggered upon a user's disconnection
     */

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String tenant = (String) headerAccessor.getSessionAttributes().get("tenant"); // ndrysho nga Integer në String

        if (username != null && tenant != null) {
            log.info("User disconnected: {}", username);

            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .tenant(tenant)
                    .build();

            messagingTemplate.convertAndSend("/topic/tenant-" + tenant, chatMessage);
        }
    }


}
