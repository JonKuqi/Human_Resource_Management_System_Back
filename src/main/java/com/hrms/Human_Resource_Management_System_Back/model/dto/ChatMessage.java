package com.hrms.Human_Resource_Management_System_Back.model.dto;


import com.hrms.Human_Resource_Management_System_Back.model.types.MessageType;
import lombok.*;

import java.awt.*;

/**
 * Data transfer object (DTO) for chat messages exchanged over WebSocket.
 * <p>
 * - type: The type of the message (e.g., CHAT, JOIN, LEAVE).
 * - content: The actual text content of the message.
 * - sender: The username of the message sender.
 * - timestamp: The time when the message was sent.
 * </p>
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String timestamp;

}
