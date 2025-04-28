package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class MessageDto {
    private String email;
    private String sender;
    private String message;
    private String timestamp;
}
