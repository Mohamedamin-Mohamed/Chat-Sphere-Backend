package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class MailjetDto {
    private String recipientEmail;
    private String subject;
    private String bodyContent;
}
