package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class TwilioVerifyDto {
    private String email;
    private String phoneNumber;
}
