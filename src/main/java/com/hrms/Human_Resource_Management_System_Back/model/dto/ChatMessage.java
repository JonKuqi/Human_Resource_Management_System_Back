package com.hrms.Human_Resource_Management_System_Back.model.dto;


import com.hrms.Human_Resource_Management_System_Back.model.types.MessageType;
import lombok.*;

import java.awt.*;


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
