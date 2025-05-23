package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import com.hrms.Human_Resource_Management_System_Back.model.dto.ChatMessage;
import com.hrms.Human_Resource_Management_System_Back.model.types.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.security.Principal;

/**
 * WebSocket controller for handling chat-related messaging within a tenant.
 * <p>
 * This controller manages real-time chat functionality using STOMP over WebSocket,
 * ensuring that all communication is scoped per tenant. Each message is routed to
 * a topic that corresponds to the tenant the user belongs to.
 * </p>
 * <p>
 * Topics format: <code>/topic/tenant-{tenantId}</code>
 * </p>
 */
@Controller
public class ChatController {

    /**
     * Template used to send messages to specific WebSocket destinations.
     */

    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Constructor-based injection for the messaging template.
     *
     * @param messagingTemplate the template used for sending WebSocket messages
     */

    @Autowired
    public ChatController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Handles chat message delivery within the tenant.
     * <p>
     * This method listens to <code>/app/chat.sendMessage</code>. It reads the sender's username
     * and tenant from the session, verifies tenant match, and forwards the message to
     * <code>/topic/tenant-{tenant}</code> to ensure messages are only shared among users
     * of the same tenant.
     * </p>
     *
     * @param chatMessage     the chat message received from the client
     * @param headerAccessor  accessor for WebSocket session attributes
     */

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage,
                            SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionTenant = (String) headerAccessor.getSessionAttributes().get("tenant");

        if (sessionTenant != null && sessionTenant.equals(chatMessage.getTenant())) {
            chatMessage.setSender(username);
            messagingTemplate.convertAndSend("/topic/tenant-" + sessionTenant, chatMessage);
        }}

    /**
     * Handles user join event for the chat session.
     * <p>
     * This method listens to <code>/app/chat.addUser</code>. It registers the user's username
     * and tenant into the WebSocket session attributes, and sends a JOIN message to the corresponding
     * tenant topic to notify other members of the same tenant.
     * </p>
     *
     * @param chatMessage     the join message received from the client
     * @param headerAccessor  accessor for WebSocket session attributes
     */

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage,
                        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("tenant", chatMessage.getTenant());

        chatMessage.setType(MessageType.JOIN);
        messagingTemplate.convertAndSend("/topic/tenant-" + chatMessage.getTenant(), chatMessage);
    }

}
