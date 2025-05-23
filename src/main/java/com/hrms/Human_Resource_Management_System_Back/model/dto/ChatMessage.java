package com.hrms.Human_Resource_Management_System_Back.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.Human_Resource_Management_System_Back.model.types.MessageType;
import lombok.*;

import java.awt.*;

/**
 * Data transfer object (DTO) for chat messages exchanged over WebSocket.
 * <p>
 * This DTO is used to encapsulate message data exchanged between clients and the server
 * via WebSocket in a tenant-specific chat system.
 * </p>
 * <p>
 * Fields:
 * <ul>
 *     <li><b>type</b>: The type of the message (e.g., CHAT, JOIN, LEAVE).</li>
 *     <li><b>content</b>: The actual text content of the message.</li>
 *     <li><b>sender</b>: The username of the message sender.</li>
 *     <li><b>timestamp</b>: The time when the message was sent.</li>
 *     <li><b>tenant</b>: The tenant schema name (e.g., <code>tenant_abc123</code>) to which the message belongs.</li>
 *     <li><b>userTenantId</b>: The internal ID of the user within the tenant schema.</li>
 * </ul>
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private String tenant;
    private Integer userTenantId;
}
