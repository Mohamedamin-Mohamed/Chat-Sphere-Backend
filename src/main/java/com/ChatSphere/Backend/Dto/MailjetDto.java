package com.ChatSphere.Backend.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MailjetDto {
    private String recipientEmail;
    private String subject;
    private String bodyContent;
}
