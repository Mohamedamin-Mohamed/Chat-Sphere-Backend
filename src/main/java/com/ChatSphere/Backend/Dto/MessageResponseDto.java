package com.ChatSphere.Backend.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDto {
    private String messageId;
    private String email;
    private String sender;
    private String message;
    private String timestamp;
}
