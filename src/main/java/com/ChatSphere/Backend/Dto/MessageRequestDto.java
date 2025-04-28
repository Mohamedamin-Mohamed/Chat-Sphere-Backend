package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class MessageRequestDto {
    private String email;
    private String sender;
    private String message;
    private String timestamp;
}
